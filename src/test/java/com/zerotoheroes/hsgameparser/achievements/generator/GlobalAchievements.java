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
        List<RawAchievement> manaSpents = buildManaSpents();
        List<RawAchievement> enemyMinionsDeads = buildEnemyMinionsDeads();
        List<RawAchievement> totalDurations = buildTotalDurations();
        List<RawAchievement> result = Stream.of(damageDealtToEnemyHeroes, manaSpents, enemyMinionsDeads, totalDurations)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(35);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildDamageDealtToEnemyHeroes() throws Exception {
        List<Integer> targetDamages = newArrayList(200, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000);
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
                .text("Deal damage to enemy heroes in all game modes (%%globalStats."
                        + GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO
                        + "."
                        + GlobalStats.Context.GLOBAL
                        + "%% damage dealt so far)")
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

    private List<RawAchievement> buildManaSpents() throws Exception {
        List<Integer> targetManas = newArrayList(200, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000);
        return targetManas.stream()
                .map(targetMana -> buildManaSpent(targetMana, targetMana == 200))
                .collect(Collectors.toList());
    }

    private RawAchievement buildManaSpent(int targetMana, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("global_mana_spent_" + targetMana)
                .type("global_mana_spent")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(targetMana)
                .name("Mana Charged")
                .displayName("Achievement completed: Mana Charged (" + formatter.format(targetMana) + " mana crystals spent)")
                .displayCardId("CS2_013t")
                .displayCardType("minion")
                .difficulty("rare")
                .text("Spend mana crystals in all game modes (%%globalStats."
                        + GlobalStats.Key.TOTAL_MANA_SPENT
                        + "."
                        + GlobalStats.Context.GLOBAL
                        + "%% mana spent so far)")
                .completedText("Spent " + formatter.format(targetMana) + " mana crystals")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_MANA_SPENT,
                                GlobalStats.Context.GLOBAL,
                                "" + targetMana)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildEnemyMinionsDeads() throws Exception {
        List<Integer> targetMinionsDead = newArrayList(50, 100, 200, 500, 1_000, 2_000, 5_000, 10_000, 50_000, 100_000);
        return targetMinionsDead.stream()
                .map(minionsDead -> buildEnemyMinionsDead(minionsDead, minionsDead == 50))
                .collect(Collectors.toList());
    }

    private RawAchievement buildEnemyMinionsDead(int minionsDead, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("global_enemy_minions_dead_" + minionsDead)
                .type("global_enemy_minions_dead")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(minionsDead)
                .name("Assassin")
                .displayName("Achievement completed: Assassin (" + formatter.format(minionsDead) + " enemy minions dead)")
                .displayCardId("CS2_076")
                .displayCardType("spell")
                .difficulty("rare")
                .text("Kill enemy minions in all game modes (%%globalStats."
                        + GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH
                        + "."
                        + GlobalStats.Context.GLOBAL
                        + "%% minions killed so far)")
                .completedText("Killed " + formatter.format(minionsDead) + " enemy minions")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH,
                                GlobalStats.Context.GLOBAL,
                                "" + minionsDead)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildTotalDurations() throws Exception {
        List<Integer> targetDurations = newArrayList(1, 10, 20, 50, 100, 200, 300, 400, 500);
        return targetDurations.stream()
                .map(targetDuration -> buildTotalDuration(targetDuration, targetDuration == 1))
                .collect(Collectors.toList());
    }

    private RawAchievement buildTotalDuration(int hoursPlayed, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("global_total_duration_" + hoursPlayed)
                .type("global_total_duration")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(hoursPlayed)
                .name("Addicted")
                .displayName("Achievement completed: Addicted (" + formatter.format(hoursPlayed) + " hours spent in matches)")
                .displayCardId("UNG_028t")
                .displayCardType("spell")
                .difficulty("rare")
                .text("Spend time in matches in all game modes (%%globalStats."
                        + GlobalStats.Key.TOTAL_DURATION
                        + "."
                        + GlobalStats.Context.GLOBAL
                        + "%% hours spent in matches so far)")
                .completedText("Spent " + formatter.format(hoursPlayed) + " hours in matches")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_DURATION,
                                GlobalStats.Context.GLOBAL,
                                "" + hoursPlayed)
                        ).build()
                ))
                .build();
    }
}
