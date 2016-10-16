package com.zerotoheroes.hsgameparser.metadata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.zerotoheroes.hsgameentities.enums.GameTag;
import com.zerotoheroes.hsgameentities.enums.PlayState;
import com.zerotoheroes.hsgameentities.replaydata.GameData;
import com.zerotoheroes.hsgameentities.replaydata.GameHelper;
import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameentities.replaydata.entities.FullEntity;
import com.zerotoheroes.hsgameentities.replaydata.entities.PlayerEntity;
import com.zerotoheroes.hsgameentities.replaydata.gameactions.TagChange;
import com.zerotoheroes.hsgameparser.db.Card;
import com.zerotoheroes.hsgameparser.db.CardsList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameParser {

	private static CardsList cardsList;

	public GameParser() throws Exception {
		if (cardsList == null) {
			cardsList = CardsList.create();
			log.debug("Created cards list with " + cardsList.getCards().size() + " cards");
		}
	}

	public GameMetaData getMetaData(HearthstoneReplay replay) {
		log.debug("retrieving metadata for " + replay);

		GameMetaData meta = new GameMetaData();

		GameHelper helper = new GameHelper();
		helper.setGame(replay.getGames().get(0));

		// Find out the last turn number
		List<TagChange> turnChanges = helper.getFlatData().stream().filter(d -> (d instanceof TagChange))
				.map(p -> (TagChange) p).filter(t -> t.getEntity() == 1 && t.getName() == GameTag.TURN.getIntValue())
				.collect(Collectors.toList());
		TagChange lastTurn = turnChanges.get(turnChanges.size() - 1);
		int numberOfTurns = (int) Math.ceil(lastTurn.getValue() / 2.0);
		meta.setNumberOfTurns(numberOfTurns);

		// Game duration
		List<GameData> timestampedData = helper.getFlatData().stream()
				.filter(d -> !StringUtils.isEmpty(d.getTimestamp())).collect(Collectors.toList());
		Collections.sort(timestampedData, (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));

		// Get the first and last moments
		Date first = parseDate(timestampedData.get(0).getTimestamp());
		Date last = parseDate(timestampedData.get(timestampedData.size() - 1).getTimestamp());
		meta.setDurationInSeconds((int) ((last.getTime() - first.getTime()) / 1000));

		// Win status
		// Get the main player. The first one being the "current player" is us
		// The data will be ordered enough for us, as the tag is part of an
		// Action block
		PlayerEntity player = helper.getMainPlayer();
		int ourEntityId = player.getId();
		// Now find the tag change that tells us if we won
		List<TagChange> tagChanges = helper.filterGameData(TagChange.class);
		Optional<TagChange> winner = tagChanges.stream().filter(
				t -> t.getName() == GameTag.PLAYSTATE.getIntValue() && t.getValue() == PlayState.WON.getIntValue())
				.findFirst();

		String winStatus = "unknown";
		if (winner.isPresent()) {
			if (ourEntityId == winner.get().getEntity()) {
				winStatus = "won";
			}
			else {
				winStatus = "lost";
			}
		}
		// No winner means a tie or a disconnect
		else {
			Optional<TagChange> tied = tagChanges.stream().filter(
					t -> t.getName() == GameTag.PLAYSTATE.getIntValue() && t.getValue() == PlayState.TIED.getIntValue())
					.findFirst();
			if (tied.isPresent()) {
				winStatus = "tied";
			}
		}
		meta.setWinStatus(winStatus);
		
		// Filter player data
		List<PlayerEntity> players = helper.getPlayers();
		PlayerEntity player1 = players.stream().filter(p -> p.getId() == ourEntityId).findFirst().get();
		PlayerEntity player2 = players.stream().filter(p -> p.getId() != ourEntityId).findFirst().get();

		meta.setPlayerName(player1.getName());
		meta.setPlayerClass(getPlayerClass(replay, player1));
		meta.setOpponentName(player2.getName());
		meta.setOpponentClass(getPlayerClass(replay, player2));

		log.debug("retrieved meta " + meta);

		return meta;
	}

	private Date parseDate(String timestamp) {
		Date result = null;
		// Try various formats
		try {
			result = new SimpleDateFormat("HH:mm:ss").parse(timestamp);
		}
		catch (ParseException e) {
			try {
				result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX").parse(timestamp);
			}
			catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;
	}

	private String getPlayerClass(HearthstoneReplay replay, PlayerEntity player) {
		List<GameData> data = replay.getGames().get(0).getData();

		int playerEntityId = player.getTags().stream().filter(t -> t.getName() == GameTag.HERO_ENTITY.getIntValue())
				.findFirst().get().getValue();
		FullEntity playerEntity = data.stream().filter(d -> (d instanceof FullEntity)).map(e -> (FullEntity) e)
				.filter(e -> e.getId() == playerEntityId).findFirst().get();

		Card playerCard = cardsList.getCards().stream()
				.filter(c -> playerEntity.getCardId().equalsIgnoreCase(c.getId())).findFirst().get();

		return playerCard.getPlayerClass().toLowerCase();
	}
}
