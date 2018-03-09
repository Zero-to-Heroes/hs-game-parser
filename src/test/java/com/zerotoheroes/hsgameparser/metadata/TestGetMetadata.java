package com.zerotoheroes.hsgameparser.metadata;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGetMetadata implements WithAssertions {

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
		checkMeta("win_status_error.xml", 6, 271, "won", "coin");
		checkMeta("from log file.xml", 9, 687, "won", "play");
		checkMeta("brawl won.xml", 5, 193, "won", "coin");

		// Also test with hsreplay.net files
		checkMeta("worgen otk 32 dmg.xml", 11, 890, "won", "play");
	}

	@Test
	public void testParsingIssues() throws Exception {
		checkMeta("bugparsing/bug_parsing.xml", "RalleHead", "Козюта");
		checkMeta("bugparsing/bug_parsing2.xml", "Побег из храма", "outcold58");
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

	private void checkMeta(String fileName, String playerName, String opponentName) throws Exception {
		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay);
		assertThat(metaData.getPlayerName()).isEqualTo(playerName);
		assertThat(metaData.getOpponentName()).isEqualTo(opponentName);
	}
}
