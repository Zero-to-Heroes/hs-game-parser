package com.zerotoheroes.hsgameparser;

public interface GameEvents {

    void gameStart();

    void gameEnd();

    void turnStart(int turnNumber);

    void turnEnd();

    void minionSummoned(String minionName);

    void spellPlayed(String spellName);

    void healSelf(int health);

    void damageTaken(int damage);

    void damageDealtToOpponent(int damage);

    // add other events you want to track here
}
