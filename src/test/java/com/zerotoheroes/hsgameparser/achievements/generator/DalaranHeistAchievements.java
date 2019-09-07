package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_DRAWN_OR_RECEIVED_IN_HAND;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_OPPONENT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PASSIVE_BUFF;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENE_CHANGED_TO_GAME;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.DALARAN_HEIST;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.DALARAN_HEIST_HEROIC;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.DALARAN_HEIST_NORMAL;

public class DalaranHeistAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> boss = buildBossAchievements();
        List<RawAchievement> treasures = buildTreasureAchievements();
        List<RawAchievement> passives = buildPassiveAchievements();
        List<RawAchievement> result = Stream.of(boss, treasures, passives)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(this::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildBossAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("DALA_BOSS"))
                .filter(card -> "Hero".equals(card.getType()))
                // Remove bartenders
                .filter(card -> !Lists.newArrayList("DALA_BOSS_98h", "DALA_BOSS_99h").contains(card.getId()))
                .filter(card -> !Lists.newArrayList(
                        "DALA_BOSS_06dk" // Scourgelord Drazzik, which comes from Drazzik playing his DK card in Heroic
                        ).contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = bossCards.stream()
                .flatMap(card -> buildDalaranBossEntries(card).stream())
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(300);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildDalaranBossEntries(DbCard card) {
        return Lists.newArrayList(
                buildDalaranBossEncounterEntry(card),
                buildDalaranBossVictoryEntry(card),
                buildDalaranBossHeroicEncounterEntry(card),
                buildDalaranBossHeroicVictoryEntry(card));
    }

    private RawAchievement buildDalaranBossEncounterEntry(DbCard card) {
        return buildDalaranBossEntry(card, "dalaran_heist_boss_encounter")
                .icon("boss_encounter")
                .root(true)
                .priority(0)
                .displayName("Boss met: " + card.getSafeName())
                .completedText("You met " + card.getName())
                .difficulty("common")
                .points(1)
                .requirements(Lists.newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
                        Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_END))
                .build();
    }

    private RawAchievement buildDalaranBossHeroicEncounterEntry(DbCard card) {
        return buildDalaranBossEntry(card, "dalaran_heist_boss_encounter_heroic")
                .icon("boss_encounter")
                .root(false)
                .priority(2)
                .displayName("Heroic boss met: " + card.getSafeName())
                .completedText("You heroically met " + card.getName())
                .difficulty("rare")
                .points(2)
                .requirements(Lists.newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
                        Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_HEROIC)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_END))
                .build();
    }

    private RawAchievement buildDalaranBossVictoryEntry(DbCard card) {
        return buildDalaranBossEntry(card, "dalaran_heist_boss_victory")
                .icon("boss_victory")
                .root(false)
                .priority(1)
                .displayName("Boss defeated: " + card.getSafeName())
                .completedText("You defeated " + card.getName())
                .difficulty("common")
                .points(2)
                .requirements(Lists.newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement buildDalaranBossHeroicVictoryEntry(DbCard card) {
        return buildDalaranBossEntry(card, "dalaran_heist_boss_victory_heroic")
                .icon("boss_victory")
                .root(false)
                .priority(3)
                .displayName("Heroic boss defeated: " + card.getSafeName())
                .completedText("You heroically defeeated " + card.getName())
                .difficulty("rare")
                .points(3)
                .requirements(Lists.newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_HEROIC)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement.RawAchievementBuilder buildDalaranBossEntry(DbCard card, String type) {
        return RawAchievement.builder()
                .id(type + "_" + card.getId())
                .type("dalaran_heist_boss_" + card.getId())
                .name(card.getSafeName())
                .text(this.sanitize(card.getText()))
                .emptyText(null)
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase());
    }

    private List<RawAchievement> buildTreasureAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
                        || Arrays.asList("").contains(card.getId()))
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
                .filter(card -> !card.getName().contains("Anomaly - "))
                .filter(card -> !Arrays.asList(
                        "DALA_501", // Cheerful Spirit?
                        "DALA_502", // Infuriated Banker
                        "DALA_503", "DALA_504", // Kirin Tor XXX
                        "DALA_714a", "DALA_714b", "DALA_714c", // Candles twinspell
                        "DALA_716t", // 50 dmg bomb token,
                        "DALA_724d",
                        "DALA_728d",
                        "DALA_744d",
                        "DALA_746d",
                        "DALA_800", "DALA_801", "DALA_802", "DALA_803", "DALA_804", "DALA_805", "DALA_806", "DALA_807", "DALA_808", // Random decks
                        "DALA_829", "DALA_829t", // Mage's starting power
                        "DALA_830", "DALA_831", "DALA_832", "DALA_832t", "DALA_833", "DALA_833t", // Some stuff to change the run contents.
                        "DALA_901", "DALA_902", "DALA_903", "DALA_904", "DALA_905", "DALA_906", "DALA_907", "DALA_908",
                        "DALA_909", "DALA_910", "DALA_911", "DALA_912", "DALA_913", "DALA_914" // Tavern stuff. Maybe handle them in another achievemnet?
                ).contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = treasureCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dalaran_heist_treasure_play_" + card.getId())
                        .type("dalaran_heist_treasure_play_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Treasure played: " + card.getSafeName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()))
                        .emptyText(null)
                        .completedText("You played " + card.getName())
                        .difficulty("rare")
                        .points(3)
                        .requirements(Lists.newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(Lists.newArrayList(card.getId())).build(),
                                // Special case for The Bosx
                                card.getId().equals("DALA_701")
                                        ? Requirement.builder().type(CARD_DRAWN_OR_RECEIVED_IN_HAND).values(Lists.newArrayList(card.getId())).build()
                                        : null,
                                Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST)).build()
                        ))
                        .resetEvents(Lists.newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(29);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildPassiveAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> pasiveCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
                        || Arrays.asList(
                        "GILA_506", // First Aid Kit
                        "GILA_813", // Expedite
                        "LOOTA_800", // Potion of Vitality
                        "LOOTA_801", // Crystal Gem
                        "LOOTA_803", // Scepter of Summoning
                        "LOOTA_804", // Small Backpacks
                        "LOOTA_825", // Robe of the Magi
                        "LOOTA_828", // Captured Flag
                        "LOOTA_831", // Glyph of Warding
                        "LOOTA_832", // Cloak of Invisibility
                        "LOOTA_833", // Mysterious Tome
                        "LOOTA_845", // Totem of the Dead
                        "LOOTA_846" // Battle totem
                ).contains(card.getId()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> !card.getType().equals("Enchantment"))
                .collect(Collectors.toList());
        List<RawAchievement> result = pasiveCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("dalaran_heist_passive_play_" + card.getId())
                        .type("dalaran_heist_passive_play_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Passive ability triggered: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()).replace("<b>Passive</b>", ""))
                        .emptyText(null)
                        .completedText("You triggered " + card.getName())
                        .difficulty("rare")
                        .points(3)
                        .requirements(Lists.newArrayList(
                                Requirement.builder().type(PASSIVE_BUFF).values(Lists.newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST)).build()
                        ))
                        .resetEvents(Lists.newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(29);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<String> toStrings(List<Integer> scenarioIds) {
        return scenarioIds.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @SneakyThrows
    private String serialize(RawAchievement rawAchievement) {
        return mapper.writeValueAsString(rawAchievement);
    }

    private String sanitize(String text) {
        String displayText = text == null ? "..." : text;
        return displayText
                .replace("<i>", "")
                .replace("</i>", "")
                .replace("[x]", "")
                .replace("<b>", "")
                .replace("</b>", "")
                .replace("\n", ". ");
    }
}
