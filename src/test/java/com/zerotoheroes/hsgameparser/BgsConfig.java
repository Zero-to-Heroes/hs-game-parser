package com.zerotoheroes.hsgameparser;

import com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import lombok.Builder;
import lombok.Getter;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BgsConfig implements WithAssertions {

    @Test
    public void generate_achievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("TB_BaconShop_HERO_"))
                .filter(card -> !Arrays.asList(
                        "TB_BaconShop_HERO_KelThuzad", // Kel'Thuzad
                        "TB_BaconShop_HERO_PH" // BaconPHhero
                )
                        .contains(card.getId()))
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> result = heroCards.stream()
                .map(card -> BattlegroundsHero.builder()
                        .cardId(card.getId())
                        .heroName(card.getName())
                        .powerLevel("")
                        .difficulty("")
                        .strategy("")
                        .build())
                .map(GeneralHelper::serializeWithEmpty)
                .collect(Collectors.toList());
        System.out.println(result);
    }
}

@Builder
@Getter
class BattlegroundsHero {
    private final String cardId;
    private final String heroName;
    private final String powerLevel;
    private final String difficulty;
    private final String strategy;
}