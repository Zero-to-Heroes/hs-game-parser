package com.zerotoheroes.hsgameparser.achievements.generator;

import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.GameType;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import com.zerotoheroes.hsgameparser.achievements.ScenarioIds;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_OPPONENT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_STARTING_HEALTH;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.EXCLUDED_SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.MINIONS_CONTROLLED_DURING_TURN;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PASSIVE_BUFF;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.QUALIFIER_AT_LEAST;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENE_CHANGED_TO_GAME;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.sanitize;
import static org.assertj.core.util.Lists.newArrayList;

public class TombsOfTerrorAchievements implements WithAssertions {

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> boss = buildBossAchievements();
        List<RawAchievement> plagueLords = buildPlagueLordsAchievements();
        List<RawAchievement> treasures = buildTreasureAchievements();
        List<RawAchievement> passives = buildPassiveAchievements();
        List<RawAchievement> bob = buildBobTreatsAchievements();
        List<RawAchievement> amazingPlays = buildAmazingPlaysAchievements();
        List<RawAchievement> result = Stream.of(boss, plagueLords, treasures, passives, bob, amazingPlays)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildBossAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("ULDA_BOSS"))
                .filter(card -> "Hero".equals(card.getType()))
                // Remove bartenders
                .filter(card -> !newArrayList("ULDA_BOSS_99h").contains(card.getId()))
                // Don't put the plague lords here
                .filter(card -> !newArrayList(
                        "ULDA_BOSS_37h", "ULDA_BOSS_37h2", "ULDA_BOSS_37h3",
                        "ULDA_BOSS_38h", "ULDA_BOSS_38h2", "ULDA_BOSS_38h3",
                        "ULDA_BOSS_39h", "ULDA_BOSS_39h2", "ULDA_BOSS_39h3",
                        "ULDA_BOSS_40h", "ULDA_BOSS_40h2", "ULDA_BOSS_40h3",
                        "ULDA_BOSS_67h", "ULDA_BOSS_67h2", "ULDA_BOSS_67h3"
                ).contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = bossCards.stream()
                .flatMap(card -> buildBossEntries(card).stream())
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(146);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildPlagueLordsAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
//        List<DbCard> plagueLordCards = cardsList.getDbCards().stream()
//                .filter(card -> Lists.newArrayList(
//                        "ULDA_BOSS_37h", "ULDA_BOSS_37h2", "ULDA_BOSS_37h3",
//                        "ULDA_BOSS_38h", "ULDA_BOSS_38h2", "ULDA_BOSS_38h3",
//                        "ULDA_BOSS_39h", "ULDA_BOSS_39h2", "ULDA_BOSS_39h3",
//                        "ULDA_BOSS_40h", "ULDA_BOSS_40h2", "ULDA_BOSS_40h3",
//                        "ULDA_BOSS_67h", "ULDA_BOSS_67h2", "ULDA_BOSS_67h3"
//                ).contains(card.getId()))
//                .collect(Collectors.toList());
        List<RawAchievement> result = newArrayList(
                        buildPlagueLordEntry(newArrayList(cardsList.findDbCard("ULDA_BOSS_37h"), cardsList.findDbCard("ULDA_BOSS_37h2"), cardsList.findDbCard("ULDA_BOSS_37h3"))),
                        buildPlagueLordEntry(newArrayList(cardsList.findDbCard("ULDA_BOSS_38h"), cardsList.findDbCard("ULDA_BOSS_38h2"), cardsList.findDbCard("ULDA_BOSS_38h3"))),
                        buildPlagueLordEntry(newArrayList(cardsList.findDbCard("ULDA_BOSS_39h"), cardsList.findDbCard("ULDA_BOSS_39h2"), cardsList.findDbCard("ULDA_BOSS_39h3"))),
                        buildPlagueLordEntry(newArrayList(cardsList.findDbCard("ULDA_BOSS_40h"), cardsList.findDbCard("ULDA_BOSS_40h2"), cardsList.findDbCard("ULDA_BOSS_40h3"))),
                        buildPlagueLordEntry(newArrayList(cardsList.findDbCard("ULDA_BOSS_67h")))
                ).stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(10);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildBossEntries(DbCard card) {
        return newArrayList(
                buildBossEncounterEntry(card),
                buildBossVictoryEntry(card));
//                buildDalaranBossHeroicEncounterEntry(card),
//                buildDalaranBossHeroicVictoryEntry(card));
    }

    private RawAchievement buildBossEncounterEntry(DbCard card) {
        return buildBossEntry(card, "tombs_of_terror_boss_encounter")
                .icon("boss_encounter")
                .root(true)
                .priority(0)
                .displayName("Boss met: " + card.getSafeName())
                .completedText("You met " + card.getName())
                .difficulty("common")
                .maxNumberOfRecords(1)
                .points(1)
                .requirements(newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                        Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_END))
                .build();
    }

//    private RawAchievement buildDalaranBossHeroicEncounterEntry(DbCard card) {
//        return buildDalaranBossEntry(card, "dalaran_heist_boss_encounter_heroic")
//                .icon("boss_encounter")
//                .root(false)
//                .priority(2)
//                .displayName("Heroic boss met: " + card.getSafeName())
//                .completedText("You heroically met " + card.getName())
//                .difficulty("rare")
//                .maxNumberOfRecords(1)
//                .points(2)
//                .requirements(Lists.newArrayList(
//                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
//                        Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
//                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_HEROIC)).build()
//                ))
//                .resetEvents(Lists.newArrayList(GameEvents.GAME_END))
//                .build();
//    }

    private RawAchievement buildBossVictoryEntry(DbCard card) {
        return buildBossEntry(card, "tombs_of_terror_boss_victory")
                .icon("boss_victory")
                .root(false)
                .priority(1)
                .displayName("Boss defeated: " + card.getSafeName())
                .completedText("You defeated " + card.getName())
                .difficulty("common")
                .maxNumberOfRecords(1)
                .points(2)
                .requirements(newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                        Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

//    private RawAchievement buildDalaranBossHeroicVictoryEntry(DbCard card) {
//        return buildDalaranBossEntry(card, "dalaran_heist_boss_victory_heroic")
//                .icon("boss_victory")
//                .root(false)
//                .priority(3)
//                .displayName("Heroic boss defeated: " + card.getSafeName())
//                .completedText("You heroically defeeated " + card.getName())
//                .difficulty("rare")
//                .maxNumberOfRecords(2)
//                .points(3)
//                .requirements(Lists.newArrayList(
//                        Requirement.builder().type(CORRECT_OPPONENT).values(Lists.newArrayList(card.getId())).build(),
//                        Requirement.builder().type(GAME_WON).build(),
//                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_HEROIC)).build()
//                ))
//                .resetEvents(Lists.newArrayList(GameEvents.GAME_START))
//                .build();
//    }

    private RawAchievement.RawAchievementBuilder buildBossEntry(DbCard card, String type) {
        return RawAchievement.builder()
                .id(type + "_" + card.getId())
                .type("tombs_of_terror_boss_" + card.getId())
                .name(card.getSafeName())
                .text(sanitize(card.getText()))
                .emptyText(null)
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase());
    }

    private List<RawAchievement> buildPlagueLordEntry(List<DbCard> bossForms) {
        DbCard firstForm = bossForms.get(0);
        return newArrayList(
                RawAchievement.builder()
                        .id("tombs_of_terror_boss_encounter_" + firstForm.getId())
                        .type("tombs_of_terror_boss_" + firstForm.getId())
                        .name(firstForm.getSafeName())
                        .text(sanitize(firstForm.getText()))
                        .emptyText(null)
                        .displayCardId(firstForm.getId())
                        .displayCardType(firstForm.getType().toLowerCase())
                        .icon("boss_encounter")
                        .root(true)
                        .priority(0)
                        .displayName("Plague Lord met: " + firstForm.getSafeName())
                        .completedText("You met " + firstForm.getName())
                        .difficulty("common")
                        .maxNumberOfRecords(1)
                        .points(1)
                        .requirements(newArrayList(
                                Requirement.builder()
                                        .type(CORRECT_OPPONENT)
                                        .values(newArrayList(bossForms.stream().map(DbCard::getId).collect(Collectors.toList())))
                                        .build(),
                                Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                                Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                        Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_END))
                        .build(),
                RawAchievement.builder()
                        .id("tombs_of_terror_boss_victory_" + firstForm.getId())
                        .type("tombs_of_terror_boss_" + firstForm.getId())
                        .name(firstForm.getSafeName())
                        .text(sanitize(firstForm.getText()))
                        .emptyText(null)
                        .displayCardId(firstForm.getId())
                        .displayCardType(firstForm.getType().toLowerCase())
                        .icon("boss_victory")
                        .root(false)
                        .priority(1)
                        .displayName("Plague Lord defeated: " + firstForm.getSafeName())
                        .completedText("You defeated " + firstForm.getName())
                        .difficulty("common")
                        .maxNumberOfRecords(1)
                        .points(2)
                        .requirements(newArrayList(
                                Requirement.builder().type(CORRECT_OPPONENT)
                                        // We want to grant the achievement whatever the initial form of the boss was
                                        .values(newArrayList(bossForms.stream().map(DbCard::getId).collect(Collectors.toList())))
                                        .build(),
                                Requirement.builder().type(GAME_WON).build(),
                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                                Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
                                // Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START))
                .build());
    }

    private List<RawAchievement> buildTreasureAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("ULDA_\\d{3}[a-z]?")
                        || Arrays.asList("").contains(card.getId()))
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .filter(card -> !bobsTreats().contains(card.getId()))
                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
                .filter(card -> !card.getName().contains("Twist - "))
                .filter(card -> !card.getName().contains("Anomaly - "))
                .filter(card -> !card.getName().contains(" Dummy"))
                .filter(card -> !card.getName().contains("Random Deck"))
                .filter(card -> !Arrays.asList(
                        "ULDA_026", // Servant of Siamat
                        "ULDA_030", // Sand Trap
                        "ULDA_031", // Sanctum Golem
                        "ULDA_036t", // Giant Locust
                        "ULDA_038", // Explorer Retraining
                        "ULDA_804t", // Eternal Tomb
                        "ULDA_801t", // Surprise! Murlocs!
                        "ULDA_911", // Kindle
                        "ULDA_912" // Recruit
                ).contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = treasureCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("tombs_of_terror_treasure_play_" + card.getId())
                        .type("tombs_of_terror_treasure_play_" + card.getId())
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
                        .maxNumberOfRecords(2)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                                Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                                Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(62);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildBobTreatsAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bobTreats = cardsList.getDbCards().stream()
                .filter(card -> bobsTreats().contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = bobTreats.stream()
                .map(card -> RawAchievement.builder()
                        .id("tombs_of_terror_treasure_play_" + card.getId())
                        .type("tombs_of_terror_bob_treat_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Bob's Treat: " + card.getSafeName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()))
                        .emptyText(null)
                        .completedText("You received " + card.getName() + " from Bob")
                        .difficulty("rare")
                        .maxNumberOfRecords(2)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                                Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                                Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(18);
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
                .filter(card -> card.getId().matches("ULDA_\\d{3}[a-z]?")
                        || Arrays.asList(
                        "DALA_746", // Elixir of Vigor
                        "DALA_747", // Elixir of Vim
                        "LOOTA_803", // Scepter of Summoning
                        "LOOTA_824", // Scrying Orb
                        "LOOTA_831" // Glyph of Warding
                ).contains(card.getId()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> !card.getType().equals("Enchantment"))
                .collect(Collectors.toList());
        List<RawAchievement> result = pasiveCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("tombs_of_terror_passive_play_" + card.getId())
                        .type("tombs_of_terror_passive_play_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Passive ability triggered: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()).replace("Passive", "").trim())
                        .emptyText(null)
                        .completedText("You triggered " + card.getName())
                        .difficulty("rare")
                        .maxNumberOfRecords(1)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(PASSIVE_BUFF).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                                Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
//                                Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(21);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildAmazingPlaysAchievements() throws Exception {
        List<RawAchievement> result = newArrayList(
                fullTinyfin(),
                unstoppable("ULDA_BOSS_37h"),
                unstoppable("ULDA_BOSS_38h"),
                unstoppable("ULDA_BOSS_39h"),
                unstoppable("ULDA_BOSS_40h"),
                unstoppable("ULDA_BOSS_67h")
        );
        assertThat(result.size()).isEqualTo(6);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private RawAchievement unstoppable(String cardId) throws Exception {
        CardsList cardsList = CardsList.create();
        DbCard card = cardsList.findDbCard(cardId);
        return RawAchievement.builder()
                .id("tombs_of_terror_amazing_plays_unstoppable_" + card.getId())
                .type("tombs_of_terror_amazing_plays_unstoppable_" + card.getId())
                .name(card.getSafeName())
                .emptyText("Defeat " + sanitize(card.getName()) + " in a single encounter")
                .completedText("You defeated " + sanitize(card.getName()) + " in a single encounter")
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase())
                .icon("boss_victory")
                .root(true)
                .priority(0)
                .displayName("Unstoppable: " + sanitize(card.getName()))
                .difficulty("epic")
                .maxNumberOfRecords(3)
                .points(20)
                .requirements(newArrayList(
                        // Only award the achievement if the boss was defeated after starting the fight with full HP
                        Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(CORRECT_STARTING_HEALTH).values(newArrayList(card.getId(), "" + card.getHealth())).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                        Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
                        // Requirement.builder().type(SCENARIO_IDS).values(toStrings(DALARAN_HEIST_NORMAL)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement fullTinyfin() {
        return RawAchievement.builder()
                .id("tombs_of_terror_amazing_plays_full_tinyfin")
                .type("tombs_of_terror_amazing_plays_full_tinyfin")
                .icon("boss_victory")
                .root(true)
                .priority(0)
                .name("Tinyfins Invasion")
                .displayName("Achievement completed: Tinyfin Invasion")
                .displayCardId("LOEA10_3")
                .displayCardType("minion")
                .emptyText("Get a board full of Murloc Tinyfins in a Tombs of Terrors game")
                .completedText("You got a board full of Mulroc Tinyfins! So cute <3")
                .difficulty("epic")
                .maxNumberOfRecords(3)
                .points(20)
                .requirements(newArrayList(
                        Requirement.builder().type(MINIONS_CONTROLLED_DURING_TURN).values(newArrayList("LOEA10_3", "7", QUALIFIER_AT_LEAST)).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.VS_AI)).build(),
                        Requirement.builder().type(EXCLUDED_SCENARIO_IDS).values(excludedScenarioIds()).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<String> bobsTreats() {
        return newArrayList(
                "ULDA_019", // Pack Mule
                "DALA_904", // Good Food
                "DALA_905", // Right Hand Man
                "DALA_906", // Round of Drinks
                "DALA_907", // Recruit a Veteran
                "DALA_908", // Tell a Story
                "DALA_909", // You're All Fired!
                "DALA_910", // The Gang's All Here
                "DALA_912", // Brood
                "DALA_913", // Tall Tales
                "ULDA_601", // Hiring Replacements
                "ULDA_602", // Study break
                "ULDA_603", // Friendly Smith
                "ULDA_604", // Party of Four
                "ULDA_605", // Fast Food
                "ULDA_606", // Work, Work!
                "ULDA_607", // House Special
                "ULDA_608" // Do the Math
        );
    }

    private List<String> excludedScenarioIds() {
        return
                Stream.of(
                        GeneralHelper.toStrings(ScenarioIds.DALARAN_HEIST),
                        GeneralHelper.toStrings(newArrayList(ScenarioIds.DUNGEON_RUN)),
                        GeneralHelper.toStrings(newArrayList(ScenarioIds.MONSTER_HUNT, ScenarioIds.MONSTER_HUNT_FINAL)),
                        GeneralHelper.toStrings(newArrayList(ScenarioIds.RUMBLE_RUN)),
                        GeneralHelper.toStrings(newArrayList(ScenarioIds.PRACTICE)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
