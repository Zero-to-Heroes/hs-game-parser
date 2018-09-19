package com.zerotoheroes.hsgameparser.metadata;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;
import com.zerotoheroes.hsgameparser.db.CardsList;
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
		gameParser = new GameParser(CardsList.create());
	}

	@Test
	public void testMetadata() throws Exception {
		// More checks on win/loss
		checkMeta("old.xml", 12, 476, "lost", "coin", "HERO_01", "HERO_07");
		checkMeta("win_status_error.xml", 6, 271, "won", "coin", "HERO_09", "HERO_06");
		checkMeta("from log file.xml", 9, 687, "won", "play", "HERO_08a", "HERO_08a");
		checkMeta("brawl won.xml", 5, 193, "won", "coin", "HERO_05", "HERO_03");
		checkMeta("bugparsing/bug_parsing2.xml", 9, 502, "lost", "play", "HERO_03a", "LOEA04_01h");

		// Also test with hsreplay.net files
		checkMeta("worgen otk 32 dmg.xml", 11, 890, "won", "play", "HERO_01", "HERO_04");
	}

	@Test
	public void testParsingIssues() throws Exception {
		checkMeta("bugparsing/puzzle.xml", "Вомбат#2497", "Dexter the Dendrologist");
		checkMeta("bugparsing/bug_parsing_23576_2.xml", "DeMarco#1198", "IGUANAPARK#1795");
		checkMeta("bugparsing/bug_parsing_23576.xml", "Rox3r#2849", "аллахакбар#2297");
		checkMeta("bugparsing/bug_parsing.xml", "RalleHead", "Козюта");
		checkMeta("bugparsing/bug_parsing2.xml", "Побег из храма", "outcold58");
	}

	private void checkMeta(String fileName, int nbTurns, int duration, String winStatus, String playCoinStatus,
	                       String playerCardId, String opponentCardId)
			throws Exception {

		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay);
		assertEquals("Incorrect number of turns", nbTurns, metaData.getNumberOfTurns());
		assertEquals("Incorrect duration", duration, metaData.getDurationInSeconds());
		assertEquals("Incorrect win status", winStatus, metaData.getResult());
		assertEquals("Incorrect play/coin status", playCoinStatus, metaData.getPlayCoin());
		assertEquals("Incorrect player card Id", playerCardId, metaData.getPlayerCardId());
		assertEquals("Incorrect opponent card Id", opponentCardId, metaData.getOpponentCardId());
	}

	private void checkMeta(String fileName, String playerName, String opponentName) throws Exception {
		HearthstoneReplay replay = gameLoader.load(fileName);
		GameMetaData metaData = gameParser.getMetaData(replay);
		assertThat(metaData.getPlayerName()).isEqualTo(playerName);
		assertThat(metaData.getOpponentName()).isEqualTo(opponentName);
	}
}
