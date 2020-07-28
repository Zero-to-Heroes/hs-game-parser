package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.FormatType.STANDARD;
import static com.zerotoheroes.hsgameparser.achievements.GameType.CASUAL;
import static com.zerotoheroes.hsgameparser.achievements.GameType.RANKED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARDS_WITH_SAME_ATTRIBUTE_PLAYED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DAMAGE_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_COST;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_NAME;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DECK_CARD_TEXT_VALUE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_MAX_TURNS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.HEALTH_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_FORMAT_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SAME_MINION_ATTACK_TIMES;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_CARDS_PLAYED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DAMAGE_DEALT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DISCARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_HERO_HEAL;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_MINIONS_SUMMONED;
import static org.assertj.core.util.Lists.newArrayList;

public class ThijsAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> thijs = buildTrackingAchievements();
        List<RawAchievement> result = Stream.of(thijs)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildTrackingAchievements() throws Exception {
        List<RawAchievement> result = newArrayList(
                buildThijs(),
                buildTeaTime(),
                buildQuickWin(),
                buildUntouchable(),
                buildWhoNeedsCards(),
                buildEchoCavern(),
                buildCloseCall(),
                buildTotalDamage(),
                buildSecrets(),
                buildNoMinions(),
                buildNoSpells(),
                buildAttackMultipleTimes(),
                buildHeal(),
                buildBigDeck()
        )
                .stream()
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(14);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private RawAchievement buildThijs() {
        return RawAchievement.builder()
                .id("thijs_thijs")
                .type("thijs_thijs")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("This is Thijs")
                .displayName("This is Thijs")
                .displayCardId("FIRESTONE_THIJS")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game with a deck containing only cards whose English name start with a T, H, I, J or S "
                        + "in "
                        + "standard (casual or ranked)")
                .completedText("Won one game with a deck containing only cards whose English name starts with a T, "
                        + "H, I, J or "
                        + "S in "
                        + "standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(DECK_CARD_NAME)
                                .values(newArrayList("30", "AT_LEAST", "STARTS_WITH", "T|H|I|J|S"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildTeaTime() {
        return RawAchievement.builder()
                .id("thijs_tea_time")
                .type("thijs_tea_time")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Tea Time")
                .displayName("Tea Time")
                .displayCardId("ULDA_504")
                .displayCardType("spell")
                .difficulty("common")
                .text("Win one game with a deck containing at least 8 cards that have the sequence \"tea\" in their "
                        + "name or text (e.g. Thoughtsteal) in standard (casual or ranked)")
                .completedText("Won one game with a deck containing at least 8 cards that have the sequence \"tea\" "
                        + "in their "
                        + "name or text (e.g. Thoughtsteal) in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(DECK_CARD_TEXT_VALUE)
                                .values(newArrayList("8", "AT_LEAST", "TEA", "CONTAINS"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildQuickWin() {
        return RawAchievement.builder()
                .id("thijs_quick_win")
                .type("thijs_quick_win")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Quick Win")
                .displayName("Thijs - Quick Win")
                .displayCardId("CS2_029")
                .displayCardType("spell")
                .difficulty("common")
                .text("Win one game before turn 6 in standard (casual or ranked)")
                .completedText("Won one game before turn 8 in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(GAME_MAX_TURNS).values(newArrayList("6")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildUntouchable() {
        return RawAchievement.builder()
                .id("thijs_untouchable")
                .type("thijs_untouchable")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Untouchable")
                .displayName("Thijs - Untouchable")
                .displayCardId("KAR_712")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game at full health in standard (casual or ranked)")
                .completedText("Won one game at full health in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DAMAGE_AT_END).values(newArrayList("0")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildWhoNeedsCards() {
        return RawAchievement.builder()
                .id("thijs_whoneedscards")
                .type("thijs_whoneedscards")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Who needs cards?")
                .displayName("Thijs - Who needs cards?")
                .displayCardId("UNG_833")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after discarding at least 6 cards in standard (casual or ranked)")
                .completedText("Won one game after discarding at least 6 cards in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(TOTAL_DISCARD).values(newArrayList("6")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildEchoCavern() {
        return RawAchievement.builder()
                .id("thijs_echo_cavern")
                .type("thijs_echo_cavern")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Echo cavern")
                .displayName("Thijs - Echo cavern")
                .displayCardId("DALA_BOSS_05p")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after playing at least 4 spells (echo copies don't count) with the same name in "
                        + "standard "
                        + "(casual or ranked)")
                .completedText("Won one game after playing at least 4 spells (echo copies don't count) with the same"
                        + " name in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(CARDS_WITH_SAME_ATTRIBUTE_PLAYED)
                                .values(newArrayList("4", "AT_LEAST", "NAME", "SPELL"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildCloseCall() {
        return RawAchievement.builder()
                .id("thijs_close_call")
                .type("thijs_close_call")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Close Call")
                .displayName("Thijs - Close Call")
                .displayCardId("CS2_181")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game with at most 3 HP remaining in standard (casual or ranked)")
                .completedText("Won one game with at most 3 HP remaining  in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(HEALTH_AT_END).values(newArrayList("3", "AT_MOST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildTotalDamage() {
        return RawAchievement.builder()
                .id("thijs_total_damage")
                .type("thijs_total_damage")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Juggernaught")
                .displayName("Thijs - Juggernaught")
                .displayCardId("GVG_056")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after dealing at least 200 damage in standard (casual or ranked)")
                .completedText("Won one game after dealing at least 200 damage in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(TOTAL_DAMAGE_DEALT).values(newArrayList("200", "AT_LEAST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildSecrets() {
        return RawAchievement.builder()
                .id("thijs_secrets")
                .type("thijs_secrets")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Secrets")
                .displayName("Thijs - Secrets")
                .displayCardId("EX1_080")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after casting at least 5 secrets in standard (casual or ranked)")
                .completedText("Won one game after casting at least 5 secrets in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(TOTAL_CARDS_PLAYED)
                                .values(newArrayList("5", "AT_LEAST", "SECRET"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildNoMinions() {
        return RawAchievement.builder()
                .id("thijs_no_minions")
                .type("thijs_no_minions")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - No minions")
                .displayName("Thijs - No minions")
                .displayCardId("EX1_345t")
                .displayCardType("spell")
                .difficulty("common")
                .text("Win one game without summoning any minion in standard (casual or ranked)")
                .completedText("Won one game without summoning any minion in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(TOTAL_MINIONS_SUMMONED)
                                .values(newArrayList("0", "AT_MOST"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildNoSpells() {
        return RawAchievement.builder()
                .id("thijs_no_spells")
                .type("thijs_no_spells")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - No spells")
                .displayName("Thijs - No spells")
                .displayCardId("DRG_073")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game without playing any spell in standard (casual or ranked)")
                .completedText("Won one game without playing any spell in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(TOTAL_CARDS_PLAYED)
                                .values(newArrayList("0", "AT_MOST", "SPELL"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildAttackMultipleTimes() {
        return RawAchievement.builder()
                .id("thijs_attack_multiple_times")
                .type("thijs_attack_multiple_times")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Persistent")
                .displayName("Thijs - Persistent")
                .displayCardId("GVG_111t")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after attacking with the same minion at least 4 times in standard (casual or "
                        + "ranked)")
                .completedText("Won one game after attacking with the same minion at least 4 times in standard (casual or ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(SAME_MINION_ATTACK_TIMES)
                                .values(newArrayList("4", "AT_LEAST"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildHeal() {
        return RawAchievement.builder()
                .id("thijs_heal")
                .type("thijs_heal")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Healed")
                .displayName("Thijs - Healed")
                .displayCardId("UNG_940t8")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game after healing your hero for at least 20 health in standard (casual or "
                        + "ranked)")
                .completedText("Won one game after healing your hero for at least 20 health in standard (casual or "
                        + "ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(TOTAL_HERO_HEAL).values(newArrayList("20", "AT_LEAST")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildBigDeck() {
        return RawAchievement.builder()
                .id("thijs_big_deck")
                .type("thijs_big_deck")
                .icon("boss_victory")
                .root(true)
                .canBeCompletedOnlyOnce(false)
                .priority(0)
                .name("Thijs - Massive")
                .displayName("Thijs - Massive")
                .displayCardId("OG_042")
                .displayCardType("minion")
                .difficulty("common")
                .text("Win one game with a deck that has at least 10 cards costing 8 or more in standard (casual or "
                        + "ranked)")
                .completedText("Won one game with a deck that has at least 10 cards costing 8 or more in standard (casual or "
                        + "ranked)")
                .maxNumberOfRecords(1)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED, CASUAL)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(DECK_CARD_COST)
                                .values(newArrayList("10", "AT_LEAST", "8", "AT_LEAST"))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }


}
