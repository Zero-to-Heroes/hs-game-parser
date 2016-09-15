package com.zerotoheroes.hsgameparser.amazingplays.rules;

import com.zerotoheroes.hsgameentities.replaydata.entities.BaseEntity;
import com.zerotoheroes.hsgameparser.amazingplays.GameEvents;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlight;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlightListener;

public abstract class AbstractGameEventsAdapter implements GameEvents {
	private final GameHighlightListener listener;

	protected AbstractGameEventsAdapter(GameHighlightListener listener) {
		this.listener = listener;
	}

	protected final void notifyHighlight(GameHighlight gameHighlight) {
		listener.notify(gameHighlight);
	}

	@Override
	public void gameStart() {

	}

	@Override
	public void gameEnd() {

	}

	@Override
	public void turnStart(int turnNumber) {

	}

	@Override
	public void turnEnd() {

	}

	@Override
	public void minionSummoned(String minionName) {

	}

	@Override
	public void spellPlayed(String spellName) {

	}

	@Override
	public void healSelf(int health) {

	}

	@Override
	public void damageTaken(BaseEntity entity, int damage) {

	}

	@Override
	public void damageDealtToOpponent(int damage) {

	}
}
