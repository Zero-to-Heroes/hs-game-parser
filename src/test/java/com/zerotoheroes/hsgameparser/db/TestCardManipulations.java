package com.zerotoheroes.hsgameparser.db;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TestCardManipulations {

    @Test
    public void test() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("LOOTA_BOSS"))
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> strings = bossCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"boss_encounter\", "
                        + "\"achievementId\": \"boss_encounter_" + card.getId() + "\", "
                        + "\"bossId\": \"" + card.getId() + "\", "
                        + "\"bossDbfId\": " + card.getDbfId() + ", "
                        + "\"difficulty\": }")
                .collect(Collectors.toList());
        System.out.println(String.join("\n", bossCards.stream().map(DbCard::toString).collect(Collectors.toList())));
        System.out.println(String.join("\n", strings));
        
    }
}
