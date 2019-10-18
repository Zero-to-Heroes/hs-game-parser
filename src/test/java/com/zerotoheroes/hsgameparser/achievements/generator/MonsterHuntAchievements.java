package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_OPPONENT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.MONSTER_HUNT_STEP;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PASSIVE_BUFF;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PLAYER_HERO;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.MONSTER_HUNT;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.MONSTER_HUNT_FINAL;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.sanitize;
import static org.assertj.core.util.Lists.newArrayList;

public class MonsterHuntAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> progression = buildProgressionAchievements();
        List<RawAchievement> boss = buildBossAchievements();
        List<RawAchievement> treasures = buildTreasureAchievements();
        List<RawAchievement> passives = buildPassiveAchievements();
        List<RawAchievement> result = Stream.of(progression, boss, treasures, passives)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildProgressionAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> Arrays.asList("GILA_400h", "GILA_500h3", "GILA_600h", "GILA_900h").contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = heroes.stream()
                .flatMap(card -> {
                    List<RawAchievement> allSteps = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        allSteps.add(buildProgressionStep(card, i));
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        result.add(generateMonsterHuntBossDefeat(cardsList));
        assertThat(result.size()).isEqualTo(8 * 4 + 1);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private RawAchievement buildProgressionStep(DbCard card, int i) {
        return RawAchievement.builder()
                .id("monster_hunt_progression_" + card.getId() + "_" + i)
                .type("monster_hunt_progression_" + card.getId())
                .icon("boss_victory")
                .root(i == 0)
                .priority(i)
                .name("Monster Hunt progression - " + card.getName() + " (" + card.getPlayerClass() + ")")
                .displayName(card.getName() + " (" + card.getPlayerClass() + ") - Cleared round " + (i + 1))
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase())
                .text(null)
                .emptyText("Clear the first round with " + card.getPlayerClass() + " to get started")
                .completedText("You cleared Monster Hunt's round " + (i + 1))
                .difficulty(i == 7 ? "epic" : "free")
                .maxNumberOfRecords(i == 7 ? 3 : 1)
                .points(1 + 2 * i)
                .requirements(newArrayList(
                        Requirement.builder().type(MONSTER_HUNT_STEP).values(newArrayList("" + i)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(PLAYER_HERO).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT, "" + MONSTER_HUNT_FINAL)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private RawAchievement generateMonsterHuntBossDefeat(CardsList cardsList) {
        DbCard card = cardsList.getDbCards().stream()
                .filter(dbCard -> dbCard.getId().equals("GILA_BOSS_61h"))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        return RawAchievement.builder()
                .id("monster_hunt_final_boss")
                .type("monster_hunt_final_boss")
                .icon("boss_victory")
                .root(true)
                .priority(0)
                .name("Final Boss - Hagatha the Witch")
                .displayName("Defeated Hagatha the Witch!")
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase())
                .text(null)
                .emptyText("Get ready to fight Hagatha the Witch!")
                .completedText("You defeated Hagatha the Witch!")
                .difficulty("legendary")
                .maxNumberOfRecords(5)
                .points(15)
                .requirements(newArrayList(
                        Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT, "" + MONSTER_HUNT_FINAL)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildBossAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("GILA_BOSS"))
                .filter(card -> !card.getId().equals("GILA_BOSS_52h2")) // Beastly Pete
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<RawAchievement> encounters = bossCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("monster_hunt_boss_encounter_" + card.getId())
                        .type("monster_hunt_boss_" + card.getId())
                        .icon("boss_encounter")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Boss met: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()))
                        .emptyText(null)
                        .completedText("You met " + card.getName())
                        .difficulty("rare")
                        .maxNumberOfRecords(1)
                        .points(2)
                        .requirements(newArrayList(
                                Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(Requirement.SCENE_CHANGED_TO_GAME).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT, "" + MONSTER_HUNT_FINAL)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        List<RawAchievement> victories = bossCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("monster_hunt_boss_victory_" + card.getId())
                        .type("monster_hunt_boss_" + card.getId())
                        .icon("boss_victory")
                        .root(false)
                        .priority(1)
                        .name(card.getName())
                        .displayName("Boss defeated: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(sanitize(card.getText()))
                        .emptyText(null)
                        .completedText("You defeated " + card.getName())
                        .difficulty("rare")
                        .maxNumberOfRecords(2)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(GAME_WON).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT, "" + MONSTER_HUNT_FINAL)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START))
                        .build())
                .collect(Collectors.toList());
        List<RawAchievement> result = Stream.concat(encounters.stream(), victories.stream())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(47 * 2);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildTreasureAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("GILA_\\d{3}[a-z]?")
                        || Arrays.asList("GILA_BOSS_23t", "GILA_BOSS_29t", "GILA_BOSS_43t", "GILA_BOSS_37t",
                        "GILA_BOSS_26t2", "GILA_BOSS_33t", "GILA_BOSS_57t",
                        "GILA_BOSS_27t") // Amalgamate
                        .contains(card.getId()))
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
                .filter(card -> !Arrays.asList("GILA_400t", "GILA_500t", "GILA_601", "GILA_604", "GILA_817t",
                        "GILA_818t", "GILA_819t", "GILA_825d").contains(card.getId()))
                .collect(Collectors.toList());
        List<RawAchievement> result = treasureCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("monster_hunt_treasure_play_" + card.getId())
                        .type("monster_hunt_treasure_play_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Treasure played: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(addClassSpecificText(card) + sanitize(card.getText()))
                        .emptyText(null)
                        .completedText("You played " + card.getName())
                        .difficulty("rare")
                        .maxNumberOfRecords(2)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT, "" + MONSTER_HUNT_FINAL)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(63);
        List<String> treasureTypes = result.stream()
                        .map(RawAchievement::getType)
                        .map(type -> "'" + type + "'")
                        .collect(Collectors.toList());
        System.out.println(String.join(",", treasureTypes));
        return result;
    }

    private List<RawAchievement> buildPassiveAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> pasiveCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("GILA_\\d{3}[a-z]?")
                        || Arrays.asList("LOOTA_825", "LOOTA_803", "LOOTA_804", "LOOTA_800", "LOOTA_824", "LOOTA_831", "LOOTA_801").contains(card.getId()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> !card.getType().equals("Enchantment"))
                .collect(Collectors.toList());
        List<RawAchievement> result = pasiveCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("monster_hunt_passive_play_" + card.getId())
                        .type("monster_hunt_passive_play_" + card.getId())
                        .icon("boss_victory")
                        .root(true)
                        .priority(0)
                        .name(card.getName())
                        .displayName("Passive ability triggered: " + card.getName())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .text(addClassSpecificText(card) + sanitize(card.getText()).replace("<b>Passive</b>", ""))
                        .emptyText(null)
                        .completedText("You triggered " + card.getName())
                        .difficulty("rare")
                        .maxNumberOfRecords(1)
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(PASSIVE_BUFF).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + MONSTER_HUNT)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(21);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private String addClassSpecificText(DbCard card) {
        if (newArrayList("GILA_513", "GILA_511", "GILA_501", "GILA_509", "GILA_503", "GILA_507", "GILA_510").contains(card.getId())) {
            return "(Tracker only) ";
        }
        if (newArrayList("GILA_605", "GILA_603", "GILA_602", "GILA_610", "GILA_612", "GILA_611").contains(card.getId())) {
            return "(Cannoneer only) ";
        }
        if (newArrayList("GILA_414", "GILA_410", "GILA_403", "GILA_411", "GILA_413", "GILA_401").contains(card.getId())) {
            return "(Houndmaster only) ";
        }
        if (newArrayList("GILA_913", "GILA_911", "GILA_901", "GILA_907", "GILA_903", "GILA_906", "GILA_910", "GILA_904").contains(card.getId())) {
            return "(Time-Tinker only) ";
        }
        return "";
    }
}
