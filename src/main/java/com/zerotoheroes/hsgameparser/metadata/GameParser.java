package com.zerotoheroes.hsgameparser.metadata;

import java.util.List;
import java.util.stream.Collectors;

import com.zerotoheroes.hsgameentities.enums.GameTag;
import com.zerotoheroes.hsgameentities.replaydata.GameData;
import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameentities.replaydata.entities.FullEntity;
import com.zerotoheroes.hsgameentities.replaydata.entities.PlayerEntity;
import com.zerotoheroes.hsgameparser.db.Card;
import com.zerotoheroes.hsgameparser.db.CardsList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameParser {

	private static CardsList cardsList;

	public GameParser() throws Exception {
		if (cardsList == null) {
			cardsList = CardsList.create();
			log.info("Created cards list with " + cardsList.getCards().size() + " cards");
		}
	}

	public GameMetaData getMetaData(HearthstoneReplay replay) {
		log.info("retrieving metadata for " + replay);

		GameMetaData meta = new GameMetaData();

		List<GameData> data = replay.getGames().get(0).getData();
		List<PlayerEntity> players = data.stream().filter(d -> (d instanceof PlayerEntity)).map(p -> (PlayerEntity) p)
				.collect(Collectors.toList());

		PlayerEntity player1 = players.stream().filter(p -> p.getPlayerId() == 1).findFirst().get();
		PlayerEntity player2 = players.stream().filter(p -> p.getPlayerId() == 2).findFirst().get();

		meta.setPlayerName(player1.getName());
		meta.setPlayerClass(getPlayerClass(replay, player1));
		meta.setOpponentName(player2.getName());
		meta.setOpponentClass(getPlayerClass(replay, player2));

		log.info("retrieved meta " + meta);

		return meta;
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
