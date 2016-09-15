package com.zerotoheroes.hsgameparser.amazingplays;

import java.util.List;

import com.zerotoheroes.hsgameentities.replaydata.entities.BaseEntity;

public class CompositeGameEvents implements GameEvents {
	private final List<GameEvents> listeners;

	public CompositeGameEvents(List<GameEvents> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void gameStart() {
		for (GameEvents listener : listeners) {
			listener.gameStart();
		}
	}

	@Override
	public void gameEnd() {
		for (GameEvents listener : listeners) {
			listener.gameEnd();
		}
	}

	@Override
	public void turnStart(int turnNumber) {
		for (GameEvents listener : listeners) {
			listener.turnStart(turnNumber);
		}
	}

	@Override
	public void turnEnd() {
		for (GameEvents listener : listeners) {
			listener.turnEnd();
		}
	}

	@Override
	public void minionSummoned(String minionName) {
		for (GameEvents listener : listeners) {
			listener.minionSummoned(minionName);
		}
	}

	@Override
	public void spellPlayed(String spellName) {
		for (GameEvents listener : listeners) {
			listener.spellPlayed(spellName);
		}
	}

	@Override
	public void healSelf(int health) {
		for (GameEvents listener : listeners) {
			listener.healSelf(health);
		}
	}

	@Override
	public void damageTaken(BaseEntity entity, int damage) {
		for (GameEvents listener : listeners) {
			listener.damageTaken(entity, damage);
		}
	}

	@Override
	public void damageDealtToOpponent(int damage) {
		for (GameEvents listener : listeners) {
			listener.damageDealtToOpponent(damage);
		}
	}
}
