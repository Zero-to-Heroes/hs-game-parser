package com.zerotoheroes.hsgameparser;

import java.io.File;

public class LogFileReader {
    private final GameEvents events;

    public LogFileReader(GameEvents events) {
        this.events = events;
    }

    public void read(File logFile) {
        // read log file and notify of turns/events etc.

        events.gameStart();

        // player turn
        events.turnStart(1);
        events.minionSummoned("Chillwind Yeti");
        events.spellPlayed("Fireball");
        events.damageDealtToOpponent(6);
        events.turnEnd();

       // opponent turn
        events.turnStart(2);
        events.minionSummoned("Loot Hoarder");
        events.spellPlayed("Flash Heal");
        events.healSelf(5);
        events.turnEnd();
    }
}
