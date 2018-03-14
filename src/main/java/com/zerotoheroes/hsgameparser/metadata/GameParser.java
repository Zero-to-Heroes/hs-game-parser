package com.zerotoheroes.hsgameparser.metadata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.zerotoheroes.hsgameentities.enums.GameTag;
import com.zerotoheroes.hsgameentities.enums.PlayState;
import com.zerotoheroes.hsgameentities.replaydata.GameData;
import com.zerotoheroes.hsgameentities.replaydata.GameHelper;
import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameentities.replaydata.entities.BaseEntity;
import com.zerotoheroes.hsgameentities.replaydata.entities.FullEntity;
import com.zerotoheroes.hsgameentities.replaydata.entities.PlayerEntity;
import com.zerotoheroes.hsgameentities.replaydata.gameactions.TagChange;
import com.zerotoheroes.hsgameparser.db.Card;
import com.zerotoheroes.hsgameparser.db.CardsList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class GameParser {

	@Getter
	private static CardsList cardsList;

	static {
		try {
			cardsList = CardsList.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Created cards list with " + cardsList.getCards().size() + " cards. ");
		try {
			log.debug("Text card is " + cardsList.fromDbfId(31));
		}
		catch (Exception e) {
			log.error("Could not call fromDbfId");
			e.printStackTrace();
		}
	}

	public GameMetaData getMetaData(HearthstoneReplay replay) throws InvalidGameReplayException {
		log.debug("retrieving metadata for " + replay);

		GameMetaData meta = new GameMetaData();

		GameHelper helper = new GameHelper();
		helper.setGame(replay.getGames().get(0));

		List<HasTag> orderedTags = new ArrayList<>();
		for (GameData gameData : helper.getFlatData()) {
			if (gameData instanceof TagChange) {
				orderedTags.add(HasTag.of((TagChange) gameData));
			}
			else if (gameData instanceof BaseEntity) {
				orderedTags.addAll(HasTag.from((BaseEntity) gameData));
			}
		}

		// Find out the first turn number - if it's not 1, no point in parsing
		// the metadata
		List<HasTag> turnTags = orderedTags.stream()
				.filter(t -> t.getEntity() == 1 && t.getName() == GameTag.TURN.getIntValue())
				.collect(Collectors.toList());

		HasTag firstTurn = turnTags.isEmpty() ? null : turnTags.get(0);
		if (firstTurn == null || firstTurn.getValue() != 1) {
			throw new InvalidGameReplayException("first registered turn is " + firstTurn);
		}

		// Find out the last turn number
		HasTag lastTurn = turnTags.get(turnTags.size() - 1);
		int numberOfTurns = (int) Math.ceil(lastTurn.getValue() / 2.0);
		meta.setNumberOfTurns(numberOfTurns);
		if (numberOfTurns == 0) {
			throw new InvalidGameReplayException("Should never have 0 turns");
		}

		// Game duration
		List<GameData> timestampedData = helper.getFlatData().stream()
				.filter(d -> !StringUtils.isEmpty(d.getTimestamp()))
				.collect(Collectors.toList());
		timestampedData.sort(Comparator.comparing(GameData::getTimestamp));

		// Get the first and last moments
		if (timestampedData.size() > 0) {
			Date first = parseDate(timestampedData.get(0).getTimestamp());
			Date last = parseDate(timestampedData.get(timestampedData.size() - 1).getTimestamp());
			meta.setDurationInSeconds((int) ((last.getTime() - first.getTime()) / 1000));
		}

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
		meta.setResult(winStatus);

		// Filter player data
		List<PlayerEntity> players = helper.getPlayers();
		PlayerEntity player1 = players.stream().filter(p -> p.getId() == ourEntityId).findFirst().get();
		PlayerEntity player2 = players.stream().filter(p -> p.getId() != ourEntityId).findFirst().get();

		meta.setPlayerName(player1.getName());
		meta.setPlayerClass(getPlayerClass(replay, player1));
		meta.setOpponentName(player2.getName());
		meta.setOpponentClass(getPlayerClass(replay, player2));

		// Find if we're on the coin or on the play
		// The first player to draw 4 cards is on the coin
		HasTag drawFourCardsTag = orderedTags.stream()
				.filter(t -> t.getValue() == 4 && t.getName() == GameTag.NUM_CARDS_DRAWN_THIS_TURN.getIntValue())
				.findFirst()
				// The only case where the second player doesn't draw the coin is Adventures, where the AI
				// doesn't have a Coin - so we're playing first in any case
				// TODO: this probably needs to be improved
				.orElse(new HasTag(-1, -1, -1));
		// System.out.println("coin player id " + coinPlayerId + " our " +
		// ourEntityId);
		meta.setPlayCoin(drawFourCardsTag.getEntity() == ourEntityId ? "coin" : "play");

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

	@Getter
	@RequiredArgsConstructor
	@ToString
	private static class HasTag {

		private final int entity;
		private final int name;
		private final int value;

		static HasTag of(TagChange tagChange) {
			return new HasTag(tagChange.getEntity(), tagChange.getName(), tagChange.getValue());
		}

		static List<HasTag> from(BaseEntity entity) {
			return entity.getTags().stream()
					.map(t -> new HasTag(entity.getId(), t.getName(), t.getValue()))
					.collect(Collectors.toList());
		}
	}
}
