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
    public static final String PLAYER_HERO = "PLAYER_HERO";
    public static final String CORRECT_OPPONENT = "CORRECT_OPPONENT";
    public static final String SCENE_CHANGED_TO_GAME = "SCENE_CHANGED_TO_GAME";
    public static final String CARD_PLAYED_OR_CHANGED_ON_BOARD = "CARD_PLAYED_OR_CHANGED_ON_BOARD";
    public static final String CARD_DRAWN_OR_RECEIVED_IN_HAND = "CARD_DRAWN_OR_RECEIVED_IN_HAND";
    public static final String PASSIVE_BUFF = "PASSIVE_BUFF";
    public static final String SCENARIO_IDS = "SCENARIO_IDS";
    public static final String MULLIGAN_DONE = "MULLIGAN_DONE";
    public static final String CARD_PLAYED_OR_ON_BOARD_AT_GAME_START = "CARD_PLAYED_OR_ON_BOARD_AT_GAME_START";

    private String type;
    private List<String> values;
}
