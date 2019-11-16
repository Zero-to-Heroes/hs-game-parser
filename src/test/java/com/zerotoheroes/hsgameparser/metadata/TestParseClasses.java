package com.zerotoheroes.hsgameparser.metadata;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;
import com.zerotoheroes.hsgameparser.db.CardsList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestParseClasses {

	private GameLoader gameLoader;
	private GameParser gameParser;

	@Before
	public void setup() throws Exception {
		gameLoader = new GameLoader();
		gameParser = new GameParser(CardsList.create());
	}

	@Test
	public void testParseClasses() throws Exception {

		checkMeta("class detection.xml", "priest", "shaman");
	}

	private void checkMeta(String fileName, String playerClass, String opponentClass) throws Exception {

		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay, null);

		assertEquals("incorrect player class", playerClass, metaData.getPlayerClass());
		assertEquals("incorrect opponent class", opponentClass, metaData.getOpponentClass());
	}
}
