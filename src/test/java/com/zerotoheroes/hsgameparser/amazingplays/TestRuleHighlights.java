package com.zerotoheroes.hsgameparser.amazingplays;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;
import com.zerotoheroes.hsgameparser.GameLoader;
import com.zerotoheroes.hsgameparser.amazingplays.reader.ReplayReader;
import com.zerotoheroes.hsgameparser.amazingplays.rules.MostDamageDealtRule;

public class TestRuleHighlights {

	private GameLoader gameLoader;
	private ReplayReader replayReader;

	@Before
	public void setup() {
		gameLoader = new GameLoader();
	}

	@Test
	public void testHighlights() throws Exception {

		HearthstoneReplay replay = gameLoader.load("worgen otk 32 dmg.xml");

		GameHighlightListener aggregator = new GameHighlightListener() {

			@Override
			public void notify(GameHighlight gameHighlight) {
				assertEquals("invalid maximum damage computed", Integer.valueOf(31),
						Integer.valueOf(gameHighlight.getData()));
			}
		};

		List<GameEvents> events = new ArrayList<>();
		events.add(new MostDamageDealtRule(aggregator).configure(30));

		replayReader = new ReplayReader(new CompositeGameEvents(events));

		replayReader.read(replay);
	}
}
