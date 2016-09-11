package com.zerotoheroes.hsgameparser;

import java.util.ArrayList;
import java.util.List;

public class RuleFactory {

    public GameEvents create(GameHighlightListener listener) {
        List<GameEvents> events = new ArrayList<>();
        events.add(new BiggestHealRule(listener));
        events.add(new MostDamageDealtRule(listener));
        return new CompositeGameEvents(events);
    }
}
