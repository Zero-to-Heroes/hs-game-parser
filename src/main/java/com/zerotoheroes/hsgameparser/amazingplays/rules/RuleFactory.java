package com.zerotoheroes.hsgameparser.amazingplays.rules;

import java.util.ArrayList;
import java.util.List;

import com.zerotoheroes.hsgameparser.amazingplays.CompositeGameEvents;
import com.zerotoheroes.hsgameparser.amazingplays.GameEvents;
import com.zerotoheroes.hsgameparser.amazingplays.GameHighlightListener;

public class RuleFactory {

    public GameEvents create(GameHighlightListener listener) {
        List<GameEvents> events = new ArrayList<>();
        events.add(new BiggestHealRule(listener));
        events.add(new MostDamageDealtRule(listener));
        return new CompositeGameEvents(events);
    }
}
