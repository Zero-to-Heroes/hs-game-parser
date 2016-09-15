package com.zerotoheroes.hsgameparser.amazingplays.rules;

import com.zerotoheroes.hsgameparser.amazingplays.GameHighlight;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlightListener;

public class BiggestHealRule extends AbstractGameEventsAdapter {
    private int currentTurn;
    private int currentTurnHealAmount;
    private int maxHealTurn;
    private int maxTurnHealAmount;

    public BiggestHealRule(GameHighlightListener listener) {
        super(listener);
    }

    @Override
    public void gameEnd() {
        String description = String.format("Healed for total of %d", maxTurnHealAmount);
        GameHighlight highlight = new GameHighlight(maxHealTurn, description);
        notifyHighlight(highlight);
    }

    @Override
    public void turnStart(int turnNumber) {
        currentTurn = turnNumber;
        currentTurnHealAmount = 0;
    }

    @Override
    public void turnEnd() {
        if (currentTurnHealAmount > maxTurnHealAmount) {
            maxHealTurn = currentTurn;
            maxTurnHealAmount = currentTurnHealAmount;
        }
    }

    @Override
    public void healSelf(int health) {
        currentTurnHealAmount += health;
    }
}
