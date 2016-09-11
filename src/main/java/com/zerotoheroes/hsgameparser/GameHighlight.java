package com.zerotoheroes.hsgameparser;

public class GameHighlight {
    private final int turn;
    private final String description;

    public GameHighlight(int turn, String description) {
        this.turn = turn;
        this.description = description;
    }

    public int getTurn() {
        return turn;
    }

    public String getDescription() {
        return description;
    }
}
