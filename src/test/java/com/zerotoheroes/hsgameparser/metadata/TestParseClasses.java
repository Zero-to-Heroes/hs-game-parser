package com.zerotoheroes.hsgameparser.metadata;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;

public class TestParseClasses {

	private GameLoader gameLoader;
	private GameParser gameParser;

	@Before
	public void setup() throws Exception {
		gameLoader = new GameLoader();
		gameParser = new GameParser();
	}

	@Test
	public void testParseClasses() throws Exception {

		checkMeta("class detection.xml", "priest", "shaman");
	}

	private void checkMeta(String fileName, String playerClass, String opponentClass) throws Exception {

		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay);

		assertEquals("incorrect player class", playerClass, metaData.getPlayerClass());
		assertEquals("incorrect opponent class", opponentClass, metaData.getOpponentClass());
	}
}
