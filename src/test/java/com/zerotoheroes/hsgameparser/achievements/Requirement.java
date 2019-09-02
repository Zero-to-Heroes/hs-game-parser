package com.zerotoheroes.hsgameparser.achievements;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Requirement {
    public static final String DUNGEON_RUN_STEP = "DUNGEON_RUN_STEP";
    public static final String MONSTER_HUNT_STEP = "MONSTER_HUNT_STEP";
    public static final String RUMBLE_RUN_STEP = "RUMBLE_RUN_STEP";
    public static final String GAME_WON = "GAME_WON";
    public static final String GAME_TIE = "GAME_TIE";
    public static final String PLAYER_HERO = "PLAYER_HERO";
    public static final String CORRECT_OPPONENT = "CORRECT_OPPONENT";
    public static final String SCENE_CHANGED_TO_GAME = "SCENE_CHANGED_TO_GAME";
    public static final String CARD_PLAYED_OR_CHANGED_ON_BOARD = "CARD_PLAYED_OR_CHANGED_ON_BOARD";
    public static final String CARD_DRAWN_OR_RECEIVED_IN_HAND = "CARD_DRAWN_OR_RECEIVED_IN_HAND";
    public static final String PASSIVE_BUFF = "PASSIVE_BUFF";
    public static final String SCENARIO_IDS = "SCENARIO_IDS";
    public static final String GAME_TYPE = "GAME_TYPE";
    public static final String RANKED_FORMAT_TYPE = "RANKED_FORMAT_TYPE";
    public static final String RANKED_MIN_RANK = "RANKED_MIN_RANK";
    public static final String HEALTH_AT_END = "HEALTH_AT_END";
    public static final String ARMOR_AT_END = "ARMOR_AT_END";
    public static final String FULL_HEALTH_AT_END = "FULL_HEALTH_AT_END";
    public static final String GAME_MIN_TURNS = "GAME_MIN_TURNS";
    public static final String MULLIGAN_DONE = "MULLIGAN_DONE";
    public static final String CARD_PLAYED_OR_ON_BOARD_AT_GAME_START = "CARD_PLAYED_OR_ON_BOARD_AT_GAME_START";
    public static final String TOTAL_DAMAGE_TAKEN = "TOTAL_DAMAGE_TAKEN";
    public static final String TOTAL_DISCARD = "TOTAL_DISCARD";
    public static final String TOTAL_HERO_HEAL = "TOTAL_HERO_HEAL";

    private String type;
    private List<String> values;
}
