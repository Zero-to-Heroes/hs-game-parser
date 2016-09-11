package com.zerotoheroes.hsgameparser;

public class MostDamageDealtRule extends AbstractGameEventsAdapter {
    private int maxTurnHealAmount;

    public MostDamageDealtRule(GameHighlightListener  listener) {
        super(listener);
    }
}
