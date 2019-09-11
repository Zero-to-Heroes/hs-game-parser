package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static List<String> toStrings(List<Integer> scenarioIds) {
        return scenarioIds.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @SneakyThrows
    public static String serialize(RawAchievement rawAchievement) {
        return mapper.writeValueAsString(rawAchievement);
    }

    public static String sanitize(String text) {
        String displayText = text == null ? "..." : text;
        return displayText
                .replace("<i>", "")
                .replace("</i>", "")
                .replace("[x]", "")
                .replace("<b>", "")
                .replace("</b>", "")
                .replace("$", "")
                .replace("\n", " ");
    }
}
