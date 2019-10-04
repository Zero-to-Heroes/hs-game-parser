package com.zerotoheroes.hsgameparser.achievements;

import org.assertj.core.util.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class ScenarioIds {
    public static final int PRACTICE = 263;
    public static final int DUNGEON_RUN = 2663;
    public static final int MONSTER_HUNT = 2706;
    public static final int MONSTER_HUNT_FINAL = 2821;
    public static final int RUMBLE_RUN = 2890;
    public static final int DALARAN_HEIST_CHAPTER_1 = 3005;
    public static final int DALARAN_HEIST_CHAPTER_2 = 3188;
    public static final int DALARAN_HEIST_CHAPTER_3 = 3189;
    public static final int DALARAN_HEIST_CHAPTER_4 = 3190;
    public static final int DALARAN_HEIST_CHAPTER_5 = 3191;
    public static final int DALARAN_HEIST_CHAPTER_1_HEROIC = 3328;
    public static final int DALARAN_HEIST_CHAPTER_2_HEROIC = 3329;
    public static final int DALARAN_HEIST_CHAPTER_3_HEROIC = 3330;
    public static final int DALARAN_HEIST_CHAPTER_4_HEROIC = 3331;
    public static final int DALARAN_HEIST_CHAPTER_5_HEROIC = 3332;

    // 3438 is Bob?
    // 3431 is for lord of wrath
    // 3432 is plague of flame
    public static final int TOMBS_OF_TERROR_CHAPTER_1 = 3428;
    public static final int TOMBS_OF_TERROR_CHAPTER_2 = 3429;
    public static final int TOMBS_OF_TERROR_CHAPTER_3 = 3430;
    public static final int TOMBS_OF_TERROR_CHAPTER_4 = 3431;
    public static final int TOMBS_OF_TERROR_CHAPTER_5 = 3432;
    public static final int TOMBS_OF_TERROR_BOB = 3438;
    public static final int TOMBS_OF_TERROR_CHAPTER_1_HEROIC = 3433;
    public static final int TOMBS_OF_TERROR_CHAPTER_2_HEROIC = 3434;
    public static final int TOMBS_OF_TERROR_CHAPTER_3_HEROIC = 3435;
    public static final int TOMBS_OF_TERROR_CHAPTER_4_HEROIC = 3436;
    public static final int TOMBS_OF_TERROR_CHAPTER_5_HEROIC = 3437;
    public static final int TOMBS_OF_TERROR_BOB_HEROIC = 3439;

    public static final List<Integer> DALARAN_HEIST_NORMAL = Lists.newArrayList(
            DALARAN_HEIST_CHAPTER_1,
            DALARAN_HEIST_CHAPTER_2,
            DALARAN_HEIST_CHAPTER_3,
            DALARAN_HEIST_CHAPTER_4,
            DALARAN_HEIST_CHAPTER_5);
    public static final List<Integer> DALARAN_HEIST_HEROIC = Lists.newArrayList(
            DALARAN_HEIST_CHAPTER_1_HEROIC,
            DALARAN_HEIST_CHAPTER_2_HEROIC,
            DALARAN_HEIST_CHAPTER_3_HEROIC,
            DALARAN_HEIST_CHAPTER_4_HEROIC,
            DALARAN_HEIST_CHAPTER_5_HEROIC);

    public static final List<Integer> TOMBS_OF_TERROR_NORMAL = Lists.newArrayList(
            TOMBS_OF_TERROR_CHAPTER_1,
            TOMBS_OF_TERROR_CHAPTER_2,
            TOMBS_OF_TERROR_CHAPTER_3,
            TOMBS_OF_TERROR_CHAPTER_4,
            TOMBS_OF_TERROR_CHAPTER_5,
            TOMBS_OF_TERROR_BOB);
    public static final List<Integer> TOMBS_OF_TERROR_HEROIC = Lists.newArrayList(
            TOMBS_OF_TERROR_CHAPTER_1_HEROIC,
            TOMBS_OF_TERROR_CHAPTER_2_HEROIC,
            TOMBS_OF_TERROR_CHAPTER_3_HEROIC,
            TOMBS_OF_TERROR_CHAPTER_4_HEROIC,
            TOMBS_OF_TERROR_CHAPTER_5_HEROIC,
            TOMBS_OF_TERROR_BOB_HEROIC);

    public static final List<Integer> DALARAN_HEIST = Lists.newArrayList(DALARAN_HEIST_NORMAL, DALARAN_HEIST_HEROIC).stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

    public static final List<Integer> TOMBS_OF_TERROR = Lists.newArrayList(TOMBS_OF_TERROR_NORMAL, TOMBS_OF_TERROR_HEROIC).stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
}
