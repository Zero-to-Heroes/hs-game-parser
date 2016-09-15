package com.zerotoheroes.hsgameparser.amazingplays.reader;

import java.util.List;

import com.zerotoheroes.hsgameentities.replaydata.Game;
import com.zerotoheroes.hsgameentities.replaydata.GameData;
import com.zerotoheroes.hsgameentities.replaydata.GameHelper;
import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameentities.replaydata.gameactions.Action;
import com.zerotoheroes.hsgameentities.replaydata.gameactions.TagChange;
import com.zerotoheroes.hsgameparser.amazingplays.GameEvents;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReplayReader {

	private final GameHelper helper;

	private final TagChangeReader tagChange;

	// private final GameEvents events;

	public ReplayReader(GameEvents events) {
		// this.events = events;
		helper = new GameHelper();
		tagChange = new TagChangeReader(events, helper);
	}

	public void read(HearthstoneReplay replay) {
		// read replay file and notify of turns/events etc.
		if (replay.getGames() == null || replay.getGames().isEmpty()) { return; }

		// For now support only a single game
		Game game = replay.getGames().get(0);
		helper.setGame(game);

		List<GameData> data = game.getData();

		for (GameData gameData : data) {
			readData(gameData);
		}
	}

	private void readData(GameData gameData) {
		if (gameData instanceof TagChange) {
			tagChange.read((TagChange) gameData);
		}

		if (gameData instanceof Action) {
			for (GameData data : ((Action) gameData).getData()) {
				readData(data);
			}
		}
	}
}
