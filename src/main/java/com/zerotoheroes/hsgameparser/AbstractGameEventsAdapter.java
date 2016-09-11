package com.zerotoheroes.hsgameparser;

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
    public void damageTaken(int damage) {

    }

    @Override
    public void damageDealtToOpponent(int damage) {

    }
}
