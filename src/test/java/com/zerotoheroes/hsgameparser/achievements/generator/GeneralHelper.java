package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralHelper {

    private static ObjectMapper mapper = buildObjectMapper();
    private static ObjectMapper mapperWithEmpty = new ObjectMapper();

    public static ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    public static List<String> toStrings(List<Integer> scenarioIds) {
        return scenarioIds.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @SneakyThrows
    public static String serialize(Object object) {
        return mapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static String serializeWithEmpty(Object object) {
        return mapperWithEmpty.writeValueAsString(object);
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
