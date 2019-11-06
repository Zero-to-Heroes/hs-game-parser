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

import static com.zerotoheroes.hsgameparser.achievements.GameType.BATTLEGROUNDS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.BATTLEGROUNDS_FINISH;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.CARD_PLAYED_OR_CHANGED_ON_BOARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GLOBAL_STAT;
import static com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper.sanitize;
import static org.assertj.core.util.Lists.newArrayList;

public class BattlegroundsAchievements implements WithAssertions {

    @Test
    public void generate_achievements() throws Exception {
//        List<RawAchievement> heroes = buildHeroesAchievements();
//        List<RawAchievement> minions = buildMinionsAchievements();
        List<RawAchievement> competitives = buildCompetitiveAchievements();
        List<RawAchievement> trackings = buildTrackingAchievements();
        List<RawAchievement> minions = buildMinionsPlays();
        List<RawAchievement> result = Stream.of(trackings, competitives, minions)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildCompetitiveAchievements() throws Exception {
        List<RawAchievement> heroFinishes = buildHeroFinishes();
        List<RawAchievement> result =
                Stream.of(
                        heroFinishes
                )
                        .flatMap(List::stream)
                        .sorted(Comparator.comparing(RawAchievement::getId))
                        .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(72);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildMinionsPlays() throws Exception {
        CardsList cardsList = CardsList.create();
        List<List<String>> minionsIds = newArrayList(
                newArrayList("CFM_315", "TB_BaconUps_093"), // Alleycat
                newArrayList("EX1_162", "TB_BaconUps_088"), // Dire Wolf ALpha
                newArrayList("BOT_445", "TB_BaconUps_002"), // Mecharoo
                newArrayList("GVG_103", "TB_BaconUps_094"), // Micro Machine
                newArrayList("EX1_509", "TB_BaconUps_011"), // Murloc Tidecaller
                newArrayList("EX1_506", "TB_BaconUps_003"), // Murloc Tidehunter
                newArrayList("ICC_038"), // Righteous Protector
                newArrayList("UNG_073", "TB_BaconUps_061"), // Rockpool Hunter
                newArrayList("OG_221", "TB_BaconUps_014"), // Selfless Hero
                newArrayList("CS2_065", "TB_BaconUps_059t"), // Voidwalker
                newArrayList("LOOT_013"), // Vulgar Homonculus
                newArrayList("BGS_004", "TB_BaconUps_079"), // Wrath Weaver

                newArrayList("GVG_085"), // Annoy-o-Tron
                newArrayList("EX1_556", "TB_BaconUps_006"), // Harvest Golem
                newArrayList("BOT_606", "TB_BaconUps_028"), // Kaboom Bot
                newArrayList("KAR_005", "TB_BaconUps_004"), // Kindly Grandmother
                newArrayList("GVG_048", "TB_BaconUps_066"), // Metaltooth Leaper
                newArrayList("BGS_025", "TB_BaconUps_019"), // Mounted Raptor
                newArrayList("EX1_507", "TB_BaconUps_008"), // Murloc Warleader
                newArrayList("BGS_001", "TB_BaconUps_062"), // Nathrezim Overseer
                newArrayList("GIL_681"), // Nightmare Amalgam
                newArrayList("EX1_062", "TB_BaconUps_036"), // Old Murk-Eye
                newArrayList("BGS_028", "TB_BaconUps_077"), // Pogo-Hopper
                newArrayList("CFM_316", "TB_BaconUps_027"), // Rat Pack
                newArrayList("EX1_531", "TB_BaconUps_043"), // Scavenging Hyena
                newArrayList("GVG_058"), // Shielded Minibot
                newArrayList("OG_256", "TB_BaconUps_025"), // Spawn of N'Zoth
                newArrayList("KAR_095", "TB_BaconUps_063"), // Zoobot

                newArrayList("GVG_062"), // Cobalt Guardian
                newArrayList("EX1_103", "TB_BaconUps_064"), // Coldlight Seer
                newArrayList("AT_121", "TB_BaconUps_037"), // Crowd Favorite
                newArrayList("CFM_610", "TB_BaconUps_070"), // Crystalweaver
                newArrayList("DS1_070", "TB_BaconUps_068"), // Houndmaster
                newArrayList("BRM_006", "TB_BaconUps_030"), // Imp Gang Boss
                newArrayList("OG_216", "TB_BaconUps_026"), // Infested Wolf
                newArrayList("DAL_575", "TB_BaconUps_034"), // Khadgar
                newArrayList("BGS_017", "TB_BaconUps_086"), // Pack Leader
                newArrayList("ULD_179", "TB_BaconUps_038"), // Phalanx Commander
                newArrayList("BGS_023", "TB_BaconUps_035"), // Piloted Shredder
                newArrayList("OG_145"), // Psych-o-Tron
                newArrayList("BOT_312", "TB_BaconUps_032"), // Replicating Menace
                newArrayList("GVG_055", "TB_BaconUps_069"), // Screwjank Clunker
                newArrayList("OG_123", "TB_BaconUps_095"), // Shifter Zerus
                newArrayList("BGS_002", "TB_BaconUps_075"), // Soul Juggler
                newArrayList("UNG_037", "TB_BaconUps_031"), // Tortollan Shellraiser

                newArrayList("BOT_911", "TB_BaconUps_099"), // Annoy-o-Module
                newArrayList("ICC_858", "TB_BaconUps_047"), // Bolvar
                newArrayList("LOOT_078"), // Cave Hydra
                newArrayList("EX1_093", "TB_BaconUps_009"), // Defender of Argus
                newArrayList("GIL_655", "TB_BaconUps_033"), // Festeroot Hulk
                newArrayList("GVG_027", "TB_BaconUps_044"), // Iron Sensei
                newArrayList("GVG_106", "TB_BaconUps_046"), // Junkbot
                newArrayList("KAR_702", "TB_BaconUps_073"), // Menagerie Magician
                newArrayList("BGS_024", "TB_BaconUps_050"), // Piloted Sky Golem
                newArrayList("BOT_218", "TB_BaconUps_041"), // Security Rover
                newArrayList("EX1_185"), // Siegebreaker
                newArrayList("EX1_577"), // The Beast
                newArrayList("DAL_077"), // Toxfin
                newArrayList("CFM_816", "TB_BaconUps_074"), // Virmen Sensei

                newArrayList("BGS_010", "TB_BaconUps_083"), // Annihilan Battlemaster
                newArrayList("FP1_031", "TB_BaconUps_093"), // Baron Rivendare
                newArrayList("LOE_077"), // Brann Bronzebeard
                newArrayList("BGS_018", "TB_BaconUps_085"), // Goldrinn
                newArrayList("TRL_232", "TB_BaconUps_051"), // Ironhide Direhorn
                newArrayList("BGS_009", "TB_BaconUps_082"), // Lightfang Enforcer
                newArrayList("GVG_021", "TB_BaconUps_060"), // Mal'Ganis
                newArrayList("BOT_537", "TB_BaconUps_039"), // Mechano-Egg
                newArrayList("UNG_937", "TB_BaconUps_089"), // Primalfin Lookout
                newArrayList("UNG_010", "TB_BaconUps_052"), // Sated Threshadon
                newArrayList("EX1_534", "TB_BaconUps_049"), // Savannah Highmane
                newArrayList("ICC_807", "TB_BaconUps_072"), // Strongshell Scavenger
                newArrayList("OG_300", "TB_BaconUps_058"), // The BOogeymonster

                newArrayList("GVG_113"), // Foe Reaper 4000
                newArrayList("UNG_089", "TB_BaconUps_084"), // Gentle Megasaur
                newArrayList("BGS_008", "TB_BaconUps_057"), // Ghastcoiler
                newArrayList("BGS_012", "TB_BaconUps_087"), // Kangor's Apprentice
                newArrayList("FP1_010"), // Maexxna
                newArrayList("BGS_021", "TB_BaconUps_090"), // Mama Bear
                newArrayList("GVG_114", "TB_BaconUps_080"), // Sneed's Old Shredder
                newArrayList("LOOT_368", "TB_BaconUps_059"), // Voidlord
                newArrayList("BGS_022") // Zapp Slywick
        );
        List<List<DbCard>> mininionCards = minionsIds.stream()
                .map(ids -> ids.size() == 1
                        ? newArrayList(cardsList.findDbCard(ids.get(0)))
                        : newArrayList(cardsList.findDbCard(ids.get(0)), cardsList.findDbCard(ids.get(1))))
                .collect(Collectors.toList());
        List<RawAchievement> result = mininionCards.stream()
                .map(minions -> {
                    DbCard root = minions.get(0);
                    return minions.stream()
                                .map(card -> RawAchievement.builder()
                                        .id("battlegrounds_minion_play_" + card.getId())
                                        .type("battlegrounds_minion_play_" + root.getId())
                                        .icon("boss_victory")
                                        .root(card.getId().equals(root.getId()))
                                        .priority(card.getId().equals(root.getId()) ? 0 : 1)
                                        .name(card.getName())
                                        .displayName((card.getId().equals(root.getId()) ? "Minion played: " : "Triple played: ") + card.getName())
                                        .displayCardId(card.getId())
                                        .displayCardType(card.getType().toLowerCase())
                                        .text(sanitize(card.getText()))
                                        .emptyText(null)
                                        .completedText((card.getId().equals(root.getId()) ? "Minion played: " : "Triple played: ") + card.getName())
                                        .difficulty("rare")
                                        .maxNumberOfRecords(2)
                                        .points(3)
                                        .requirements(newArrayList(
                                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                                                Requirement.builder().type(GAME_TYPE).values(newArrayList(BATTLEGROUNDS)).build()
                                        ))
                                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
                                        .build())
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(147);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildHeroFinishes() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("TB_BaconShop_HERO_"))
                .filter(card -> !Arrays.asList(
                        "TB_BaconShop_HERO_27", // Sindragosa
                        "TB_BaconShop_HERO_40", // Sir Finley
                        "TB_BaconShop_HERO_42", // Elise
                        "TB_BaconShop_HERO_43", // Brann Bronzebeard
                        "TB_BaconShop_HERO_KelThuzad", // Kel'Thuzad
                        "TB_BaconShop_HERO_PH" // BaconPHhero
                )
                        .contains(card.getId()))
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<RawAchievement> result = heroCards.stream()
                .map(hero -> buildHeroFinishes(hero))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        assertThat(result.size()).isEqualTo(24 * 3);
        return result;
    }

    private List<RawAchievement> buildHeroFinishes(DbCard card) {
        // Hero played, finish 4, win
        // Hero played -> can simply look for CARD_PLAYED with the hero
        RawAchievement heroPlayed = heroFinishBuilder(card, "play")
                .root(true)
                .priority(0)
                .displayName("Hero played: " + sanitize(card.getName()))
                .completedText("You played " + sanitize(card.getName()))
                .text("Take part in a Battlegrounds with " + sanitize(card.getName()))
                .maxNumberOfRecords(1)
                .difficulty("common")
                .points(1)
                .requirements(newArrayList(
                        Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(BATTLEGROUNDS)).build()
                ))
                .build();
        RawAchievement finishTop4 = heroFinishBuilder(card, "finish_4")
                .priority(1)
                .displayName("Finished top 4 with " + sanitize(card.getName()))
                .completedText("You finished top 4 " + sanitize(card.getName()))
                .text("Finish in the top 4 of a Battlegrounds with " + sanitize(card.getName()))
                .maxNumberOfRecords(3)
                .difficulty("rare")
                .points(10)
                .requirements(newArrayList(
                        Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(BATTLEGROUNDS_FINISH).values(newArrayList("4", "AT_LEAST")).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(BATTLEGROUNDS)).build()
                ))
                .build();
        RawAchievement win = heroFinishBuilder(card, "finish_1")
                .priority(2)
                .displayName("Battlegrounds won with " + sanitize(card.getName()))
                .completedText("You won a Battlegrounds with " + sanitize(card.getName()))
                .text("Win a Battlegrounds with " + sanitize(card.getName()))
                .maxNumberOfRecords(4)
                .difficulty("epic")
                .points(20)
                .requirements(newArrayList(
                        Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
                        Requirement.builder().type(BATTLEGROUNDS_FINISH).values(newArrayList("1", "AT_LEAST")).build(),
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(BATTLEGROUNDS)).build()
                ))
                .build();
        return newArrayList(heroPlayed, finishTop4, win);

    }

    private RawAchievement.RawAchievementBuilder heroFinishBuilder(DbCard card, String type) {
        return RawAchievement.builder()
                .id("battlegrounds_hero_" + type + "_" + card.getId())
                .type("battlegrounds_hero_" + card.getId())
                .icon("boss_encounter")
                .root(false)
                .name(sanitize(card.getName()))
                .displayCardId(card.getId())
                .displayCardType(card.getType().toLowerCase())
                .emptyText(null)
                .resetEvents(newArrayList(GameEvents.GAME_START));
    }

    private List<RawAchievement> buildTrackingAchievements() throws Exception {
        List<RawAchievement> totalMatches = buildTotalMatches();
        List<RawAchievement> totalDurations = buildTotalDurations();
        List<RawAchievement> manaSpents = buildCoinSpents();
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
        assertThat(result.size()).isEqualTo(36);
        List<String> types = result.stream()
                .map(RawAchievement::getType)
                .map(type -> "'" + type + "'")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(String.join(",", types));
        return result;
    }

    private List<RawAchievement> buildDamageDealtToEnemyHeroes() throws Exception {
        List<Integer> targetDamages = newArrayList(50, 100, 200, 500, 1000, 5000, 10000, 20000);
        return targetDamages.stream()
                .map(targetDamage -> buildDamageDealtToEnemyHero(targetDamage, targetDamage == 200))
                .collect(Collectors.toList());
    }

    private RawAchievement buildDamageDealtToEnemyHero(int targetDamage, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("battlegrounds_damage_to_enemy_heroes_" + targetDamage)
                .type("battlegrounds_damage_to_enemy_heroes")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(targetDamage)
                .name("Face is the Place (Battlegrounds)")
                .displayName("Achievement completed: Face is the Place (Battlegrounds) (" + formatter.format(targetDamage) + " damage)")
                .displayCardId("DALA_BOSS_24e")
                .displayCardType("minion")
                .difficulty("rare")
                .text("Deal damage to enemy heroes in Battlegrounds (%%globalStats."
                        + GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO
                        + "."
                        + GlobalStats.Context.BATTLEGROUNDS
                        + "%% damage dealt so far)")
                .completedText("Dealt " + formatter.format(targetDamage) + " damage to enemy heroes")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_DAMAGE_TO_ENEMY_HERO,
                                GlobalStats.Context.BATTLEGROUNDS,
                                "" + targetDamage)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildCoinSpents() throws Exception {
        List<Integer> targetCoins = newArrayList(200, 500, 1000, 5000, 10_000, 20_000, 50_000, 100_000);
        return targetCoins.stream()
                .map(targetMana -> buildCoinSpent(targetMana, targetMana == 200))
                .collect(Collectors.toList());
    }

    private RawAchievement buildCoinSpent(int targetMana, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("battlegrounds_coin_spent_" + targetMana)
                .type("battlegrounds_coin_spent")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(targetMana)
                .name("Shopping!")
                .displayName("Achievement completed: Shopping (" + formatter.format(targetMana) + " coins spent)")
                .displayCardId("CS2_013t")
                .displayCardType("minion")
                .difficulty("rare")
                .text("Spend coins in Battlegrounds (%%globalStats."
                        + GlobalStats.Key.TOTAL_MANA_SPENT
                        + "."
                        + GlobalStats.Context.BATTLEGROUNDS
                        + "%% coins spent so far)")
                .completedText("Spent " + formatter.format(targetMana) + " coins")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_MANA_SPENT,
                                GlobalStats.Context.BATTLEGROUNDS,
                                "" + targetMana)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildEnemyMinionsDeads() throws Exception {
        List<Integer> targetMinionsDead = newArrayList(50, 100, 200, 500, 1_000, 2_000, 5_000);
        return targetMinionsDead.stream()
                .map(minionsDead -> buildEnemyMinionsDead(minionsDead, minionsDead == 50))
                .collect(Collectors.toList());
    }

    private RawAchievement buildEnemyMinionsDead(int minionsDead, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("battlegrounds_enemy_minions_dead_" + minionsDead)
                .type("battlegrounds_enemy_minions_dead")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(minionsDead)
                .name("Assassin")
                .displayName("Achievement completed: Assassin (Battlegrounds) (" + formatter.format(minionsDead) + " enemy minions dead)")
                .displayCardId("CS2_076")
                .displayCardType("spell")
                .difficulty("rare")
                .text("Kill enemy minions in Battlegrounds (%%globalStats."
                        + GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH
                        + "."
                        + GlobalStats.Context.BATTLEGROUNDS
                        + "%% minions killed so far)")
                .completedText("Killed " + formatter.format(minionsDead) + " enemy minions")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_ENEMY_MINIONS_DEATH,
                                GlobalStats.Context.BATTLEGROUNDS,
                                "" + minionsDead)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildTotalDurations() throws Exception {
        List<Integer> targetDurations = newArrayList(1, 10, 20, 50, 100, 200, 300);
        return targetDurations.stream()
                .map(targetDuration -> buildTotalDuration(targetDuration, targetDuration == 1))
                .collect(Collectors.toList());
    }

    private RawAchievement buildTotalDuration(int hoursPlayed, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("battlegrounds_total_duration_" + hoursPlayed)
                .type("battlegrounds_total_duration")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(hoursPlayed)
                .name("Addicted")
                .displayName("Achievement completed: Addicted (Battlegrounds) (" + formatter.format(hoursPlayed) + " hours spent in battlegrounds matches)")
                .displayCardId("UNG_028t")
                .displayCardType("spell")
                .difficulty("rare")
                .text("Spend time in Battlegrounds matches (%%globalStats."
                        + GlobalStats.Key.TOTAL_DURATION
                        + "."
                        + GlobalStats.Context.BATTLEGROUNDS
                        + "%% hours spent in matches so far)")
                .completedText("Spent " + formatter.format(hoursPlayed) + " hours in matches")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_DURATION,
                                GlobalStats.Context.BATTLEGROUNDS,
                                "" + hoursPlayed)
                        ).build()
                ))
                .build();
    }

    private List<RawAchievement> buildTotalMatches() throws Exception {
        List<Integer> targetMatches = newArrayList(10, 20, 50, 100, 200, 500);
        return targetMatches.stream()
                .map(targetMatch -> buildTotalMatch(targetMatch, targetMatch == 10))
                .collect(Collectors.toList());
    }

    private RawAchievement buildTotalMatch(int matchesPlayed, boolean isRoot) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return RawAchievement.builder()
                .id("battlegrounds_total_match_" + matchesPlayed)
                .type("battlegrounds_total_match")
                .icon("boss_victory")
                .root(isRoot)
                .canBeCompletedOnlyOnce(true)
                .priority(matchesPlayed)
                .name("I can't stop")
                .displayName("Achievement completed: I can't stop (Battlegrounds) (" + formatter.format(matchesPlayed) + " matches played)")
                .displayCardId("DALA_BOSS_53h")
                .displayCardType("minion")
                .difficulty("rare")
                .text("Play matches in Battlegrounds (%%globalStats."
                        + GlobalStats.Key.TOTAL_NUMBER_OF_MATCHES
                        + "."
                        + GlobalStats.Context.BATTLEGROUNDS
                        + "%% matches played so far)")
                .completedText("Played " + formatter.format(matchesPlayed) + " matches")
                .maxNumberOfRecords(1)
                .points(25)
                .requirements(newArrayList(
                        Requirement.builder().type(GLOBAL_STAT).values(newArrayList(
                                GlobalStats.Key.TOTAL_NUMBER_OF_MATCHES,
                                GlobalStats.Context.BATTLEGROUNDS,
                                "" + matchesPlayed)
                        ).build()
                ))
                .build();
    }
//
//    private List<RawAchievement> buildHeroesAchievements() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> heroCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().startsWith("ULDA_BOSS"))
//                .collect(Collectors.toList());
//        List<RawAchievement> result = heroCards.stream()
//                .flatMap(card -> buildHeroEntries(card).stream())
//                .sorted(Comparator.comparing(RawAchievement::getId))
//                .collect(Collectors.toList());
//        assertThat(result.size()).isEqualTo(146);
//        List<String> types = result.stream()
//                .map(RawAchievement::getType)
//                .map(type -> "'" + type + "'")
//                .distinct()
//                .collect(Collectors.toList());
//        System.out.println(String.join(",", types));
//        return result;
//    }
//
//    private List<RawAchievement> buildHeroEntries(DbCard card) {
//        return newArrayList(buildHeroEncounterEntry(card));
//    }
//
//    private RawAchievement buildHeroEncounterEntry(DbCard card) {
//        return buildHeroEntry(card, "battlegrounds_hero_encounter")
//                .icon("boss_encounter")
//                .root(true)
//                .priority(0)
//                .displayName("Hero met: " + card.getSafeName())
//                .completedText("You met " + card.getName())
//                .difficulty("common")
//                .maxNumberOfRecords(1)
//                .points(1)
//                .requirements(newArrayList(
//                        Requirement.builder().type(CORRECT_OPPONENT).values(newArrayList(card.getId())).build(),
//                        Requirement.builder().type(SCENE_CHANGED_TO_GAME).build(),
//                        Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.BATTLEGROUNDS)).build()
//                ))
//                .resetEvents(newArrayList(GameEvents.GAME_END))
//                .build();
//    }
//
//    private List<RawAchievement> buildMinionsAchievements() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> minionCards = cardsList.getDbCards().stream()
//
//                .collect(Collectors.toList());
//        List<RawAchievement> result = minionCards.stream()
//                .map(card -> RawAchievement.builder()
//                        .id("battlegrounds_minion_play_" + card.getId())
//                        .type("battlegrounds_minion_play_" + card.getId())
//                        .icon("boss_victory")
//                        .root(true)
//                        .priority(0)
//                        .name(card.getName())
//                        .displayName("Minion played: " + card.getName())
//                        .displayCardId(card.getId())
//                        .displayCardType(card.getType().toLowerCase())
//                        .text(sanitize(card.getText()))
//                        .emptyText(null)
//                        .completedText("You played " + card.getName())
//                        .difficulty("common")
//                        .maxNumberOfRecords(1)
//                        .points(2)
//                        .requirements(newArrayList(
//                                Requirement.builder().type(CARD_PLAYED_OR_CHANGED_ON_BOARD).values(newArrayList(card.getId())).build(),
//                                Requirement.builder().type(GAME_TYPE).values(newArrayList(GameType.BATTLEGROUNDS)).build()
//                        ))
//                        .resetEvents(newArrayList(GameEvents.GAME_START, GameEvents.GAME_END))
//                        .build())
//                .collect(Collectors.toList());
//        assertThat(result.size()).isEqualTo(32);
//        List<String> treasureTypes = result.stream()
//                .map(RawAchievement::getType)
//                .map(type -> "'" + type + "'")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",", treasureTypes));
//        return result;
//    }
//
//    private RawAchievement.RawAchievementBuilder buildHeroEntry(DbCard card, String type) {
//        return RawAchievement.builder()
//                .id(type + "_" + card.getId())
//                .type("battlegrounds_hero_" + card.getId())
//                .name(sanitize(card.getSafeName()))
//                .text(sanitize(card.getText()))
//                .emptyText(null)
//                .displayCardId(card.getId())
//                .displayCardType(card.getType().toLowerCase());
//    }
}
