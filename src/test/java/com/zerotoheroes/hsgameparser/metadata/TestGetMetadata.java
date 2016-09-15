package com.zerotoheroes.hsgameparser.metadata;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;

public class TestGetMetadata {

	private GameLoader gameLoader;
	private GameParser gameParser;

	@Before
	public void setup() throws Exception {
		gameLoader = new GameLoader();
		gameParser = new GameParser();
	}

	@Test
	public void testMetadata() throws Exception {

		HearthstoneReplay replay = gameLoader.load("from log file.xml");
		GameMetaData metaData = gameParser.getMetaData(replay);
		assertEquals("Incorrect number of turns", 9, metaData.getNumberOfTurns());
		assertEquals("Incorrect duration", 687, metaData.getDurationInSeconds());
		assertEquals("Incorrect win status", "won", metaData.getWinStatus());

		// Also test with hsreplay.net files
		HearthstoneReplay replay2 = gameLoader.load("worgen otk 32 dmg.xml");
		GameMetaData metaData2 = gameParser.getMetaData(replay2);
		assertEquals("Incorrect number of turns", 11, metaData2.getNumberOfTurns());
		assertEquals("Incorrect duration", 890, metaData2.getDurationInSeconds());
		assertEquals("Incorrect win status", "won", metaData2.getWinStatus());

	}
}
