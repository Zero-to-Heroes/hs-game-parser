package com.zerotoheroes.hsgameparser.achievements.generator;

import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.GlobalStats;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.Requirement.CORRECT_OPPONENT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GLOBAL_STAT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.PLAYER_CLASS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.SCENARIO_IDS;
import static com.zerotoheroes.hsgameparser.achievements.ScenarioIds.GALAKROND_HEROIC;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.sanitize;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.toStrings;
import static org.assertj.core.util.Lists.newArrayList;

public class GalakrondAchievements implements WithAssertions {

	@Test
	public void generate_achievements() throws Exception {
		List<RawAchievement> trackings = buildTrackingAchievements();
		List<RawAchievement> domination = buildDominationAchievements();
		List<RawAchievement> result = Stream.of(trackings, domination)
				.flatMap(List::stream)
				.sorted(Comparator.comparing(RawAchievement::getId))
				.collect(Collectors.toList());
		List<String> serializedAchievements = result.stream()
				.map(GeneralHelper::serialize)
				.collect(Collectors.toList());
		System.out.println(serializedAchievements);
	}

	private List<RawAchievement> buildDominationAchievements() throws Exception {
		CardsList cardsList = CardsList.create();
		List<DbCard> enemies = cardsList.getDbCards().stream()
				.filter(card -> Arrays.asList(
						"DRGA_BOSS_05h",
						"DRGA_BOSS_01h3",
						"DRGA_BOSS_10h",
						"DRGA_BOSS_25h",
						"DRGA_BOSS_13h",
						"DRGA_BOSS_09h",
						"DRGA_BOSS_18h",
						"DRGA_BOSS_06h",
						"DRGA_BOSS_19h",
						"DRGA_BOSS_22h",
						"DRGA_BOSS_31h",
						"DRGA_BOSS_11h",
						"DRGA_BOSS_08h",
						"DRGA_BOSS_07h3",
						"DRGA_BOSS_03h2_H",
						"DRGA_BOSS_26h",
						"DRGA_BOSS_27h",
						"DRGA_BOSS_20h",
						"DRGA_BOSS_23h2",
						"DRGA_BOSS_30h2",
						"DRGA_BOSS_32h",
						"DRGA_BOSS_11h2",
						"DRGA_BOSS_01h4",
						"DRGA_BOSS_24h"
				)
						.contains(card.getId()))
				.collect(Collectors.toList());
		List<RawAchievement> result = enemies.stream()
				.map(boss -> buildDomination(boss))
				.flatMap(List::stream)
				.collect(Collectors.toList());
		assertThat(result.size()).isEqualTo(24 * 9);
		List<String> types = result.stream()
				.map(RawAchievement::getType)
				.map(type -> "'" + type + "'")
				.distinct()
				.collect(Collectors.toList());
		System.out.println(String.join(",", types));
		return result;
	}

	private List<RawAchievement> buildDomination(DbCard card) {
		List<String> classes = newArrayList("hunter", "druid", "mage", "paladin", "priest", "rogue", "shaman",
				"warlock", "warrior");
		return classes.stream().map(playerClass -> RawAchievement.builder()
				.id("galakrond_domination_" + playerClass + "_" + card.getId())
				.type("galakrond_domination_" + card.getId())
				.icon("boss_encounter")
				.root(false)
				.name(sanitize(card.getName()))
				.displayCardId(card.getId())
				.displayCardType(card.getType().toLowerCase())
				.text("Defeat " + sanitize(card.getName()) + " in Heroic difficulty")
				.resetEvents(newArrayList(GameEvents.GAME_START))
				.root(playerClass.equals("hunter"))
				.priority(0)
				.displayName("Defeated " + sanitize(card.getName() + " in Heroic difficulty"))
				.completedText("You defeated " + sanitize(card.getName() + " with " + playerClass))
				.emptyText("Defeat " + sanitize(card.getName() + " in Heroic"))
				.maxNumberOfRecords(2)
				.difficulty("epic")
				.points(1)
				.requirements(newArrayList(
						Requirement.builder().type(PLAYER_CLASS).values(newArrayList(playerClass)).build(),
						Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
						Requirement.builder().type(GAME_WON).build(),
						// We need that to avoid granting the achievements in Normal difficulty
						Requirement.builder().type(SCENARIO_IDS).values(newArrayList(toStrings(GALAKROND_HEROIC)))
								.build()
				))
				.build())
				.collect(Collectors.toList());
	}

	private List<RawAchievement> buildTrackingAchievements() throws Exception {
		List<RawAchievement> totalMatches = buildTotalMatches();
		List<RawAchievement> totalDurations = buildTotalDurations();
		List<RawAchievement> manaSpents = buildManaSpent();
		List<RawAchievement> damageDealtToEnemyHeroes = buildDamageDealtToEnemyHeroes();
		List<RawAchievement> enemyMinionsDeads = buildEnemyMinionsDeads();
		List<RawAchievement> result =
				Stream.of(
						totalMatches,
						totalDurations,
						manaSpents,
						damageDealtToEnemyHeroes,
						enemyMinionsDeads
				)
						.flatMap(List::stream)
						.sorted(Comparator.comparing(RawAchievement::getId))
						.collect(Collectors.toList());
		assertThat(result.size()).isEqualTo(38);
		List<String> types = result.stream()
				.map(RawAchievement::getType)
				.map(type -> "'" + type + "'")
				.distinct()
				.collect(Collectors.toList());
		System.out.println(String.join(",", types));
		return result;
	}

	private List<RawAchievement> buildDamageDealtToEnemyHeroes() throws Exception {
		List<Integer> targetDamages = newArrayList(150, 500, 1_000, 2_000, 3_000, 5000, 10000);
		return targetDamages.stream()
				.map(targetDamage -> buildDamageDealtToEnemyHero(targetDamage, targetDamage == 150))
				.collect(Collectors.toList());
	}

	private RawAchievement buildDamageDealtToEnemyHero(int targetDamage, boolean isRoot) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		return RawAchievement.builder()
				.id("galakrond_damage_to_enemy_heroes_" + targetDamage)
				.type("galakrond_damage_to_enemy_heroes")
				.icon("boss_victory")
				.root(isRoot)
				.canBeCompletedOnlyOnce(true)
				.priority(targetDamage)
				.name("Past the Defense")
				.displayName("Past the Defense (" + formatter.format(targetDamage) + " damage)")
				.displayCardId("TB_BaconUps_038")
				.displayCardType("minion")
				.difficulty("rare")
				.text("Deal damage to enemy heroes in Galakrond's Awakening (%%globalStats."
						+ GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO
						+ "."
						+ GlobalStats.Context.GALAKROND
						+ "%% damage dealt so far)")
				.completedText("Dealt " + formatter.format(targetDamage) + " damage to enemy heroes")
				.maxNumberOfRecords(1)
				.points(25)
				.requirements(newArrayList(
						Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
								GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO,
								GlobalStats.Context.GALAKROND,
								"" + targetDamage)
						).build()
				))
				.build();
	}

	private List<RawAchievement> buildManaSpent() throws Exception {
		List<Integer> targetCoins = newArrayList(100, 300, 500, 1000, 2000, 3000, 5000, 10000);
		return targetCoins.stream()
				.map(targetMana -> buildManaSpent(targetMana, targetMana == 100))
				.collect(Collectors.toList());
	}

	private RawAchievement buildManaSpent(int targetMana, boolean isRoot) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		return RawAchievement.builder()
				.id("galakrond_coin_spent_" + targetMana)
				.type("galakrond_coin_spent")
				.icon("boss_victory")
				.root(isRoot)
				.canBeCompletedOnlyOnce(true)
				.priority(targetMana)
				.name("Shopper")
				.displayName("Shopper (" + formatter.format(targetMana) + " mana spent)")
				.displayCardId("CS2_013t")
				.displayCardType("minion")
				.difficulty("rare")
				.text("Spend mana in Galakrond's Awakening (%%globalStats."
						+ GlobalStats.Key.TOTAL_MANA_SPENT
						+ "."
						+ GlobalStats.Context.GALAKROND
						+ "%% mana spent so far)")
				.completedText("Spent " + formatter.format(targetMana) + " mana")
				.maxNumberOfRecords(1)
				.points(25)
				.requirements(newArrayList(
						Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
								GlobalStats.Key.TOTAL_MANA_SPENT,
								GlobalStats.Context.GALAKROND,
								"" + targetMana)
						).build()
				))
				.build();
	}

	private List<RawAchievement> buildEnemyMinionsDeads() throws Exception {
		List<Integer> targetMinionsDead = newArrayList(50, 100, 300, 500, 1000, 2000, 5000, 10000);
		return targetMinionsDead.stream()
				.map(minionsDead -> buildEnemyMinionsDead(minionsDead, minionsDead == 50))
				.collect(Collectors.toList());
	}

	private RawAchievement buildEnemyMinionsDead(int minionsDead, boolean isRoot) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		return RawAchievement.builder()
				.id("galakrond_enemy_minions_dead_" + minionsDead)
				.type("galakrond_enemy_minions_dead")
				.icon("boss_victory")
				.root(isRoot)
				.canBeCompletedOnlyOnce(true)
				.priority(minionsDead)
				.name("Master of the Board")
				.displayName("Master of the Board (" + formatter.format(minionsDead) + " enemy minions dead)")
				.displayCardId("AT_121")
				.displayCardType("minion")
				.difficulty("rare")
				.text("Kill enemy minions in Galakrond's Awakening (%%globalStats."
						+ GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH
						+ "."
						+ GlobalStats.Context.GALAKROND
						+ "%% minions killed so far)")
				.completedText("Killed " + formatter.format(minionsDead) + " enemy minions")
				.maxNumberOfRecords(1)
				.points(25)
				.requirements(newArrayList(
						Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
								GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH,
								GlobalStats.Context.GALAKROND,
								"" + minionsDead)
						).build()
				))
				.build();
	}

	private List<RawAchievement> buildTotalDurations() throws Exception {
		List<Integer> targetDurations = newArrayList(5, 10, 20, 50, 100, 200);
		return targetDurations.stream()
				.map(targetDuration -> buildTotalDuration(targetDuration, targetDuration == 5))
				.collect(Collectors.toList());
	}

	private RawAchievement buildTotalDuration(int hoursPlayed, boolean isRoot) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		return RawAchievement.builder()
				.id("galakrond_total_duration_" + hoursPlayed)
				.type("galakrond_total_duration")
				.icon("boss_victory")
				.root(isRoot)
				.canBeCompletedOnlyOnce(true)
				.priority(hoursPlayed)
				.name("Time is the Essence")
				.displayName("Time is the Essence (" + formatter.format(hoursPlayed) + " hours spent in galakrond's "
						+ "awakening)")
				.displayCardId("BOT_537")
				.displayCardType("spell")
				.difficulty("rare")
				.text("Spend time in Galakrond's Awakening (%%globalStats."
						+ GlobalStats.Key.TOTAL_DURATION
						+ "."
						+ GlobalStats.Context.GALAKROND
						+ "%% hours spent so far)")
				.completedText("Spent " + formatter.format(hoursPlayed) + " hours")
				.maxNumberOfRecords(1)
				.points(25)
				.requirements(newArrayList(
						Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
								GlobalStats.Key.TOTAL_DURATION,
								GlobalStats.Context.GALAKROND,
								"" + hoursPlayed)
						).build()
				))
				.build();
	}

	private List<RawAchievement> buildTotalMatches() throws Exception {
		List<Integer> targetMatches = newArrayList(10, 20, 50, 100, 200, 500, 1000, 2000, 5000);
		return targetMatches.stream()
				.map(targetMatch -> buildTotalMatch(targetMatch, targetMatch == 10))
				.collect(Collectors.toList());
	}

	private RawAchievement buildTotalMatch(int matchesPlayed, boolean isRoot) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		return RawAchievement.builder()
				.id("galakrond_total_match_" + matchesPlayed)
				.type("galakrond_total_match")
				.icon("boss_victory")
				.root(isRoot)
				.canBeCompletedOnlyOnce(true)
				.priority(matchesPlayed)
				.name("Just One More Game")
				.displayName("Just One More Game (" + formatter.format(matchesPlayed) + " enemies faced)")
				.displayCardId("TB_BaconUps_087")
				.displayCardType("minion")
				.difficulty("rare")
				.text("Face enemies in Galakrond's Awakening (%%globalStats."
						+ GlobalStats.Key.TOTAL_NUMBER_OF_MATCHES
						+ "."
						+ GlobalStats.Context.GALAKROND
						+ "%% enemies faced so far)")
				.completedText("Faced " + formatter.format(matchesPlayed) + " enemies")
				.maxNumberOfRecords(1)
				.points(25)
				.requirements(newArrayList(
						Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
								GlobalStats.Key.TOTAL_NUMBER_OF_MATCHES,
								GlobalStats.Context.GALAKROND,
								"" + matchesPlayed)
						).build()
				))
				.build();
	}
}
