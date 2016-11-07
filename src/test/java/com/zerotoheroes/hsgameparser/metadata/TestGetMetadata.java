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

		// More checks on win/loss
		checkMeta("old.xml", 12, 476, "lost", "coin");

		checkMeta("from log file.xml", 9, 687, "won", "play");

		// Also test with hsreplay.net files
		checkMeta("worgen otk 32 dmg.xml", 11, 890, "won", "play");

		// More checks on win/loss
		checkMeta("brawl won.xml", 5, 193, "won", "coin");

	}

	private void checkMeta(String fileName, int nbTurns, int duration, String winStatus, String playCoinStatus)
			throws Exception {

		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay);
		assertEquals("Incorrect number of turns", nbTurns, metaData.getNumberOfTurns());
		assertEquals("Incorrect duration", duration, metaData.getDurationInSeconds());
		assertEquals("Incorrect win status", winStatus, metaData.getResult());
		assertEquals("Incorrect play/coin status", playCoinStatus, metaData.getPlayCoin());

	}
}