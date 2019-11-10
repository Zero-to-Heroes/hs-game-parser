package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.FormatType.STANDARD;
import static com.zerotoheroes.hsgameparser.achievements.GameType.RANKED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_FORMAT_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.WINS_AGAINST_CLASS_IN_RANKED_STANDARD_IN_LIMITED_TIME;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.WIN_STREAK_LENGTH;
import static org.assertj.core.util.Lists.newArrayList;

public class CompetitiveLadderAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> winStreak = winStreaks();
        List<RawAchievement> nemesis = nemsises();
        List<RawAchievement> result =
                Stream.of(winStreak, nemesis)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> winStreaks() {
        List<Integer> winStreakLength = newArrayList(3, 4, 5, 6, 7, 8, 9, 10);
        return winStreakLength.stream()
                .map(length -> winStreak(length, length == 3))
                .collect(Collectors.toList());
    }

    private RawAchievement winStreak(int winStreakLength, boolean isRoot) {
        return RawAchievement.builder()
                .id("competitive_ladder_win_streak_" + winStreakLength)
                .type("competitive_ladder_win_streak")
                .icon("boss_victory")
                .root(isRoot)
                .priority(winStreakLength)
                .name("Win Streaks")
                .displayName("Achievement completed: Win Streaks (" + winStreakLength + " games)")
                .displayCardId("TRL_074")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win " + winStreakLength + " games in a row (without a tie or a loss) in Ranked Standard")
                .completedText("You won " + winStreakLength + " games in a row")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(WIN_STREAK_LENGTH).values(newArrayList("" + winStreakLength, "AT_LEAST", "standard", "ranked")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> nemsises() {
        List<String> classNames = newArrayList("druid", "hunter", "mage", "paladin", "priest", "rogue", "shaman", "warlock", "warrior");
        List<Integer> winStreakLength = newArrayList(3, 4, 5, 6, 7, 8);
        List<RawAchievement> result = classNames.stream()
                .flatMap(className ->  winStreakLength.stream().map(length -> nemsis(className, length, length == 3)))
                .collect(Collectors.toList());
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private RawAchievement nemsis(String className, int numberOfVictories, boolean isRoot) {
        int periodOfTimeInHours = 12;
        String icon = "";
        if (className.equals("druid")) {
            icon = "CRED_48";
        } else if (className.equals("hunter")) {
            icon = "CRED_17";
        } else if (className.equals("mage")) {
            icon = "CRED_38";
        } else if (className.equals("paladin")) {
            icon = "CRED_13";
        } else if (className.equals("priest")) {
            icon = "CRED_19";
        } else if (className.equals("rogue")) {
            icon = "CRED_01";
        } else if (className.equals("shaman")) {
            icon = "CRED_22";
        } else if (className.equals("warlock")) {
            icon = "CRED_29";
        } else if (className.equals("warrior")) {
            icon = "CRED_09";
        }
        return RawAchievement.builder()
                .id("competitive_ladder_nemesis_" + className + "_" + numberOfVictories)
                .type("competitive_ladder_nemesis_" + className)
                .icon("boss_victory")
                .root(isRoot)
                .priority(numberOfVictories)
                .name("Nemesis - " + StringUtils.capitalize(className))
                .displayName("Achievement completed: " + className + " Nemesis (" + numberOfVictories + " games)")
                .displayCardId(icon)
                .displayCardType("minion")
                .difficulty("epic")
                .emptyText("Win " + numberOfVictories + " games against " + className + " in less than " + periodOfTimeInHours + " hours in Ranked Standard")
                .completedText("You won " + numberOfVictories + " games against " + className + " in less than " + periodOfTimeInHours + " hours")
                .maxNumberOfRecords(3)
                .points(10 + 3 * numberOfVictories)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder()
                                .type(WINS_AGAINST_CLASS_IN_RANKED_STANDARD_IN_LIMITED_TIME)
                                .values(newArrayList("" + numberOfVictories, "AT_LEAST", className, "" + periodOfTimeInHours))
                                .build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }
}
