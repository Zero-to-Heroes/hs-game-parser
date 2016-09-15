package com.zerotoheroes.hsgameparser.amazingplays.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zerotoheroes.hsgameentities.replaydata.entities.BaseEntity;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlight;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlightListener;

import lombok.Data;

public class MostDamageDealtRule extends AbstractGameEventsAdapter {

	// Map to quickly access the TurnDamage of the current turn
	private final List<TurnDamage> turnDamageMap = new ArrayList<>();

	private TurnDamage currentTurnDamage;

	private int threshold;

	public MostDamageDealtRule(GameHighlightListener listener) {
		super(listener);
	}

	public MostDamageDealtRule configure(int threshold) {
		this.threshold = threshold;
		return this;
	}

	@Override
	public void turnStart(int turnNumber) {
		currentTurnDamage = new TurnDamage(turnNumber);
		turnDamageMap.add(currentTurnDamage);
	}

	@Override
	public void gameEnd() {
		for (TurnDamage turnDamage : turnDamageMap) {
			for (BaseEntity entity : turnDamage.getDamageTaken().keySet()) {
				int damage = turnDamage.getDamageTaken().get(entity);
				if (damage >= threshold) {
					GameHighlight highlight = new GameHighlight(turnDamage.turnNumber, "high-damage-taken");
					highlight.setData("" + damage);
					notifyHighlight(highlight);
				}
			}
		}
	}

	@Override
	public void damageTaken(BaseEntity entity, int damage) {
		int damageTaken = currentTurnDamage.getDamageTaken().getOrDefault(entity, 0);
		damageTaken += damage;
		currentTurnDamage.getDamageTaken().put(entity, damageTaken);
	}

	@Data
	private static class TurnDamage {

		private final int turnNumber;

		private Map<BaseEntity, Integer> damageTaken = new HashMap<>();
	}
}
