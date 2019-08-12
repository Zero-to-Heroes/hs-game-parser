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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_ON_BOARD_AT_GAME_START;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.MULLIGAN_DONE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PASSIVE_BUFF;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PLAYER_HERO;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RUMBLE_RUN_STEP;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.RUMBLE_RUN;
import static org.assertj.core.util.Lists.newArrayList;

public class RumbleRunAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> progression = buildProgressionAchievements();
        List<RawAchievement> shrines = buildShrineAchievements();
        List<RawAchievement> teammates = buildTeammatesAchievements();
        List<RawAchievement> passives = buildPassiveAchievements();
        List<RawAchievement> result = Stream.of(progression, shrines, teammates, passives)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(this::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildProgressionAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_209h_[a-zA-Z]+"))
                .collect(Collectors.toList());
        List<DbCard> shrines = buildShrines(cardsList);
        List<RawAchievement> result = heroes.stream()
                .flatMap(hero -> {
                    List<RawAchievement> allSteps = new ArrayList<>();
                    List<DbCard> heroShrines = shrines.stream()
                            .filter(shrine -> shrine.getPlayerClass().equals(hero.getPlayerClass()))
                            .collect(Collectors.toList());
                    for (DbCard shrine : heroShrines) {
                        for (int i = 0; i < 8; i++) {
                            allSteps.add(buildProgressionStep(hero, shrine, i));
                        }
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(8 * 9 * 3);
        return result;
    }

    private RawAchievement buildProgressionStep(DbCard hero, DbCard shrine, int i) {
        return RawAchievement.builder()
                .id("rumble_run_progression_" + hero.getId() + "_" + shrine.getId() + "_" + i)
                .type("rumble_run_progression")
                .name(shrine.getName() + " (" + hero.getPlayerClass() + ") - Cleared round " + (i + 1))
                .displayName(shrine.getName() + " (" + hero.getPlayerClass() + ") - Cleared round " + (i + 1))
                .emptyText("Clear the first round with " + hero.getName() + " (" + hero.getPlayerClass() + ") to get started")
                .text("Cleared round " + (i + 1))
                .displayCardId(shrine.getId())
                .displayCardType(shrine.getType().toLowerCase())
                .difficulty(i == 7 ? "epic" : "free")
                .points(1 + 2 * i)
                .requirements(newArrayList(
                        Requirement.builder().type(RUMBLE_RUN_STEP).values(newArrayList("" + i)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(PLAYER_HERO).values(newArrayList(hero.getId())).build(),
                        Requirement.builder().type(CARD_PLAYED_OR_ON_BOARD_AT_GAME_START).values(newArrayList(shrine.getId())).build(),
                        Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + RUMBLE_RUN)).build()
                ))
                .resetEvents(Lists.newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildShrineAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> shrineCards = buildShrines(cardsList);
        List<RawAchievement> result = shrineCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("rumble_run_shrine_play_" + card.getId())
                        .type("rumble_run_shrine_play")
                        .name(card.getName())
                        .displayName("Shrine played: " + card.getName())
                        .emptyText(null)
                        .text(card.getText().replace("<b>Shrine</b>", ""))
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("common")
                        .points(1)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_ON_BOARD_AT_GAME_START).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(MULLIGAN_DONE).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + RUMBLE_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(9 * 3);
        return result;
    }

    private List<RawAchievement> buildTeammatesAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> shrineCards = buildShrines(cardsList);
        List<DbCard> teammates = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
                .filter(card -> !shrineCards.contains(card))
                .filter(card -> !card.getId().equals("TRLA_107")) // Tribal Shrine
                .filter(card -> card.getType().equals("Minion"))
                .collect(Collectors.toList());
        List<RawAchievement> result = teammates.stream()
                .map(card -> RawAchievement.builder()
                        .id("rumble_run_teammate_play_" + card.getId())
                        .type("rumble_run_teammate_play")
                        .name(card.getName())
                        .displayName("Teammate joined: " + card.getName())
                        .emptyText(null)
                        .text(card.getText())
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + RUMBLE_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(54);
        return result;
    }

    private List<DbCard> buildShrines(CardsList cardsList) {
        return cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_\\d{3}"))
                .filter(card -> "Minion".equals(card.getType()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("InvisibleDeathrattle"))
                .filter(card -> card.getCost() == 0)
                .filter(card -> card.getAttack() == 0)
                .filter(card -> card.getHealth() > 1)
                .collect(Collectors.toList());
    }

    private List<RawAchievement> buildPassiveAchievements() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> pasiveCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> card.getType().equals("Spell"))
                .collect(Collectors.toList());
        List<RawAchievement> result = pasiveCards.stream()
                .map(card -> RawAchievement.builder()
                        .id("rumble_run_passive_play_" + card.getId())
                        .type("rumble_run_passive_play")
                        .name(card.getName())
                        .displayName("Passive ability triggered: " + card.getName())
                        .emptyText(null)
                        .text(card.getText().replace("<b>Passive</b>", ""))
                        .displayCardId(card.getId())
                        .displayCardType(card.getType().toLowerCase())
                        .difficulty("rare")
                        .points(3)
                        .requirements(newArrayList(
                                Requirement.builder().type(PASSIVE_BUFF).values(newArrayList(card.getId())).build(),
                                Requirement.builder().type(SCENARIO_IDS).values(newArrayList("" + RUMBLE_RUN)).build()
                        ))
                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                        .build())
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(9);
        return result;
    }

    @SneakyThrows
    private String serialize(RawAchievement rawAchievement) {
        return mapper.writeValueAsString(rawAchievement);
    }
}
