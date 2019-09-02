package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.FormatType.STANDARD;
import static com.zerotoheroes.hsgameparser.achievements.GameType.ARENA;
import static com.zerotoheroes.hsgameparser.achievements.GameType.RANKED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.ARMOR_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.FULL_HEALTH_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_MIN_TURNS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TIE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.HEALTH_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_FORMAT_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_MIN_RANK;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DAMAGE_TAKEN;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DISCARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_HERO_HEAL;
import static org.assertj.core.util.Lists.newArrayList;

public class AmazingPlaysAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> winWithOneHp = buildWinWithOneHps();
//        RawAchievement winWithFullHp = buildWinWithFullHp();
//        RawAchievement winWithoutTakingAnyDamange = buildWinWithoutTakingDamage();
//        RawAchievement gameTie = buildGameTie();
//        RawAchievement discardCards = buildDiscardCards();
//        RawAchievement totalHeal = buildTotalHeal();
        List<RawAchievement> partialResult = newArrayList(
//                winWithFullHp,
//                winWithoutTakingAnyDamange,
//                gameTie,
//                discardCards,
//                totalHeal
        );
        List<RawAchievement> result = Stream.of(winWithOneHp, partialResult)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(this::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildWinWithOneHps() {
        List<Integer> minimumRanks = newArrayList(25, 20, 15, 10, 5, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildWinWithOneHp(minimumRank, minimumRank == 25))
                .collect(Collectors.toList());
    }

    private RawAchievement buildWinWithOneHp(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_play_win_with_one_hp_" + minimumRank)
                .type("amazing_play_win_with_one_hp")
                .name("One HP matters")
                .root(isRoot)
                .priority(-minimumRank)
                .displayName("Win with 1 HP left and no armor remaining (Ranked Standard, at least rank " + minimumRank + ")")
                .displayCardId("GILA_BOSS_49p")
                .displayCardType("minion")
                .difficulty("rare")
                .text("One HP matters (Standard, rank " + minimumRank + ")")
                .emptyText("Win one game with one HP left and no armor remaining in Ranked Standard")
                .completedText("You won with one HP left at rank " + minimumRank)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(HEALTH_AT_END).values(newArrayList("1")).build(),
                        Requirement.builder().type(ARMOR_AT_END).values(newArrayList("0")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildWinWithFullHp() {
        return RawAchievement.builder()
                .id("amazing_play_win_with_full_hp")
                .type("amazing_plays")
                .name("Don't hit me")
                .displayName("Win with your full health remaining")
                .displayCardId("BRMA10_4")
                .displayCardType("minion")
                .difficulty("rare")
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, ARENA)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("19")).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(FULL_HEALTH_AT_END).build(),
                        Requirement.builder().type(GAME_MIN_TURNS).values(newArrayList("4")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildWinWithoutTakingDamage() {
        return RawAchievement.builder()
                .id("amazing_play_win_without_taking_damage")
                .type("amazing_plays")
                .name("Immune to everything")
                .displayName("Win without taking a single damage during the game")
                .displayCardId("CRED_69")
                .displayCardType("minion")
                .difficulty("epic")
                .points(10)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, ARENA)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("19")).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(TOTAL_DAMAGE_TAKEN).values(newArrayList("0")).build(),
                        Requirement.builder().type(GAME_MIN_TURNS).values(newArrayList("4")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildGameTie() {
        return RawAchievement.builder()
                .id("amazing_play_game_tie")
                .type("amazing_plays")
                .name("It's a tie!")
                .displayName("End the game with a tie (both players dying at the same time)")
                .displayCardId("KAR_044a") // Moroes' stewart
                .displayCardType("minion")
                .difficulty("legendary")
                .points(20)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, ARENA)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("19")).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_TIE).build()
                        ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildDiscardCards() {
        return RawAchievement.builder()
                .id("amazing_play_discard_cards")
                .type("amazing_plays")
                .name("Discard!")
                .displayName("Discard at least 10 cards during a game")
                .displayCardId("TRL_252")
                .displayCardType("minion")
                .difficulty("rare")
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, ARENA)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("19")).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_DISCARD).values(newArrayList("10")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildTotalHeal() {
        return RawAchievement.builder()
                .id("amazing_play_total_heal")
                .type("amazing_plays")
                .name("Healed!")
                .displayName("Heal your hero for at least 50 health in a single game")
                .displayCardId("CS1h_001_H2_AT_132")
                .displayCardType("minion")
                .text("Healed your hero for 50 health")
                .difficulty("rare")
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, ARENA)).build(),
                        Requirement.builder().type(RANKED_MIN_RANK).values(newArrayList("19")).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_HERO_HEAL).values(newArrayList("50")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<String> toStrings(List<Integer> scenarioIds) {
        return scenarioIds.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @SneakyThrows
    private String serialize(RawAchievement rawAchievement) {
        return mapper.writeValueAsString(rawAchievement);
    }
}
