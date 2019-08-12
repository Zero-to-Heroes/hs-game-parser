package com.zerotoheroes.hsgameparser.achievements;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@Getter
public class RawAchievement {
    private String id;
    private String type; // A kind of "family" for the achievements, used to group them together
    private String name;
    private String displayName;
    private String emptyText;
    private String text;
    private String displayCardId; // The image to display in the achievements screen
    private String displayCardType; // Since the art for spells is cropped differently than the art for minions
    private String difficulty;
    private float points; // How many points is this achievement worth if you complete it?
    private List<Requirement> requirements;
    private List<String> resetEvents;

    public static class RawAchievementBuilder {
        private List<Requirement> requirements;

        public RawAchievementBuilder requirements(List<Requirement> requirements) {
            this.requirements = requirements.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return this;
        }
    }
}
