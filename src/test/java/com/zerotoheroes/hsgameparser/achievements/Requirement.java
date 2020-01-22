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
    public static final String WIN_STREAK_LENGTH = "WIN_STREAK_LENGTH";
    public static final String PLAYER_HERO = "PLAYER_HERO";
    public static final String PLAYER_CLASS = "PLAYER_CLASS";
    public static final String CORRECT_OPPONENT = "CORRECT_OPPONENT";
    public static final String CORRECT_STARTING_HEALTH = "CORRECT_STARTING_HEALTH";
//    public static final String SCENE_CHANGED_TO_GAME = "SCENE_CHANGED_TO_GAME";
    public static final String CARD_PLAYED_OR_CHANGED_ON_BOARD = "CARD_PLAYED_OR_CHANGED_ON_BOARD";
    public static final String CARD_NOT_PLAYED = "CARD_NOT_PLAYED";
    public static final String CARD_DRAWN_OR_RECEIVED_IN_HAND = "CARD_DRAWN_OR_RECEIVED_IN_HAND";
    public static final String MINION_SUMMONED = "MINION_SUMMONED";
    public static final String PASSIVE_BUFF = "PASSIVE_BUFF";
    public static final String SECRET_TRIGGERED = "SECRET_TRIGGERED";
    public static final String DEATHRATTLE_TRIGGERED = "DEATHRATTLE_TRIGGERED";
    public static final String SCENARIO_IDS = "SCENARIO_IDS";
    public static final String EXCLUDED_SCENARIO_IDS = "EXCLUDED_SCENARIO_IDS";
    public static final String GAME_TYPE = "GAME_TYPE";
    public static final String RANKED_FORMAT_TYPE = "RANKED_FORMAT_TYPE";
    public static final String RANKED_MIN_RANK = "RANKED_MIN_RANK";
    public static final String HEALTH_AT_END = "HEALTH_AT_END";
    public static final String MINION_ATTACK_ON_BOARD = "MINION_ATTACK_ON_BOARD";
    public static final String ARMOR_AT_END = "ARMOR_AT_END";
    public static final String DAMAGE_AT_END = "DAMAGE_AT_END";
    public static final String FATIGUE_DAMAGE = "FATIGUE_DAMAGE";
    public static final String GAME_MIN_TURNS = "GAME_MIN_TURNS";
    public static final String MULLIGAN_DONE = "MULLIGAN_DONE";
    public static final String CARD_PLAYED_OR_ON_BOARD_AT_GAME_START = "CARD_PLAYED_OR_ON_BOARD_AT_GAME_START";
    public static final String TOTAL_DAMAGE_TAKEN = "TOTAL_DAMAGE_TAKEN";
    public static final String TOTAL_DISCARD = "TOTAL_DISCARD";
    public static final String TOTAL_HERO_HEAL = "TOTAL_HERO_HEAL";
    public static final String TOTAL_DAMAGE_DEALT = "TOTAL_DAMAGE_DEALT";
    public static final String TOTAL_ARMOR_GAINED = "TOTAL_ARMOR_GAINED";
    public static final String MINIONS_CONTROLLED_DURING_TURN = "MINIONS_CONTROLLED_DURING_TURN";

    public static final String BATTLEGROUNDS_FINISH = "BATTLEGROUNDS_FINISH";
    public static final String BATTLEGROUNDS_RANK = "BATTLEGROUNDS_RANK";
    public static final String BATTLEGROUNDS_TRIPLE_PLAY = "BATTLEGROUNDS_TRIPLE_PLAY";

    public static final String LAST_DAMAGE_DONE_BY_MINION = "LAST_DAMAGE_DONE_BY_MINION";
    public static final String RESUMMONED_RECURRING_VILLAIN = "RESUMMONED_RECURRING_VILLAIN";
    public static final String WINS_AGAINST_CLASS_IN_RANKED_STANDARD_IN_LIMITED_TIME = "WINS_AGAINST_CLASS_IN_RANKED_STANDARD_IN_LIMITED_TIME";
    public static final String BOARD_FULL_OF_SAME_LEGENDARY_MINION = "BOARD_FULL_OF_SAME_LEGENDARY_MINION";

    public static final String GLOBAL_STAT = "GLOBAL_STAT";

    public static final String QUALIFIER_AT_LEAST = "AT_LEAST";

    // We just have one requirement per deckbuilding constraint
    public static final String DECK_CLASSIC = "DECK_CLASSIC";
    public static final String DECK_MECHANIC = "DECK_MECHANIC";
    public static final String DECK_TYPE = "DECK_TYPE";
    public static final String DECK_RARITY = "DECK_RARITY";
    public static final String DECK_CARD_ATTRIBUTE_VALUE = "DECK_CARD_ATTRIBUTE_VALUE";
    public static final String DECK_CARD_TEXT_VALUE = "DECK_CARD_TEXT_VALUE";
    public static final String DECK_NO_CARD_WITH_LETTER_IN_NAME = "DECK_NO_CARD_WITH_LETTER_IN_NAME";
    public static final String DECK_CARD_TEXT_NUMBER_OF_WORDS = "DECK_CARD_TEXT_NUMBER_OF_WORDS";
    public static final String DECK_NUMBER_OF_MINIONS = "DECK_NUMBER_OF_MINIONS";

    private String type;
    private List<String> individualResetEvents;
    private List<String> values;
}
