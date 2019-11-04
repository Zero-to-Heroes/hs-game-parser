package com.zerotoheroes.hsgameparser.achievements.generator;

import com.zerotoheroes.hsgameparser.achievements.GlobalStats;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.GLOBAL_STAT;
import static org.assertj.core.util.Lists.newArrayList;

public class GlobalAchievements implements WithAssertions {

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> tracking = buildTrackingAchievements();
        List<RawAchievement> result = Stream.of(tracking)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildTrackingAchievements() throws Exception {
        List<RawAchievement> damageDealtToEnemyHeroes = buildDamageDealtToEnemyHeroes();
        List<RawAchievement> result = Stream.of(damageDealtToEnemyHeroes)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(7);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildDamageDealtToEnemyHeroes() throws Exception {
        List<Integer> targetDamages = newArrayList(200, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000);
        return targetDamages.stream()
                .map(targetDamage -> buildDamageDealtToEnemyHero(targetDamage, targetDamage == 200))
                .collect(Collectors.toList());
    }

    private RawAchievement buildDamageDealtToEnemyHero(int targetDamage, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("global_damage_to_enemy_heroes_" + targetDamage)
                .type("global_damage_to_enemy_heroes")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(targetDamage)
                .name("Face is the Place")
                .displayName("Achievement completed: Face is the Place (" + formatter.format(targetDamage) + " damage)")
                .displayCardId("DALA_BOSS_24e")
                .displayCardType("minion")
                .difficulty("rare")
                .text("Deal damage to enemy heroes (%%globalStats.total-damage-to-enemy-hero.global%% damage dealt so far)")
                .completedText("Dealt " + formatter.format(targetDamage) + " damage to enemy heroes")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO,
                                GlobalStats.Context.GLOBAL,
                                "" + targetDamage)
                        ).build()
                ))
                .build();
    }
}
