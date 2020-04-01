package com.zerotoheroes.hsgameparser.achievements.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerotoheroes.hsgameparser.achievements.GameEvents;
import com.zerotoheroes.hsgameparser.achievements.RawAchievement;
import com.zerotoheroes.hsgameparser.achievements.Requirement;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerotoheroes.hsgameparser.achievements.FormatType.STANDARD;
import static com.zerotoheroes.hsgameparser.achievements.GameType.RANKED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.ARMOR_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.DAMAGE_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_MIN_TURNS;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TIE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.GAME_WON;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.HEALTH_AT_END;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.MINIONS_CONTROLLED_DURING_TURN;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.MINION_SUMMONED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.QUALIFIER_AT_LEAST;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_FORMAT_TYPE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RANKED_MIN_LEAGUE;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.RESUMMONED_RECURRING_VILLAIN;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_ARMOR_GAINED;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DAMAGE_DEALT;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DAMAGE_TAKEN;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_DISCARD;
import static com.zerotoheroes.hsgameparser.achievements.Requirement.TOTAL_HERO_HEAL;
import static org.assertj.core.util.Lists.newArrayList;

public class AmazingPlaysAchievements implements WithAssertions {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void generate_achievements() throws Exception {
        List<RawAchievement> winWithOneHp = buildWinWithOneHps();
        List<RawAchievement> winWithFullHp = buildWinWithFullHps();
        List<RawAchievement> winWithoutTakingAnyDamange = buildWinWithoutTakingDamages();
        List<RawAchievement> gameTies = buildGameTies();
        List<RawAchievement> highkeeperRa = buildHighkeeperRas();
        List<RawAchievement> discardCards = buildDiscardCards();
        List<RawAchievement> totalHeal = buildTotalHeals();
        List<RawAchievement> totalDamages = totalDamageInGames();
        List<RawAchievement> totalArmors = totalArmorInGames();
        List<RawAchievement> desertObelisks = desertObelisks();
        List<RawAchievement> recurringVillains = recurringVillains();
        List<RawAchievement> result =
                Stream.of(
                        winWithOneHp,
                        winWithFullHp,
                        gameTies,
                        winWithoutTakingAnyDamange,
                        highkeeperRa,
                        discardCards,
                        totalHeal,
                        totalDamages,
                        totalArmors,
                        desertObelisks,
                        recurringVillains)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RawAchievement::getId))
                .collect(Collectors.toList());
        List<String> serializedAchievements = result.stream()
                .map(GeneralHelper::serialize)
                .collect(Collectors.toList());
        System.out.println(serializedAchievements);
    }

    private List<RawAchievement> buildWinWithOneHps() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildWinWithOneHp(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildWinWithOneHp(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_win_with_one_hp_" + minimumRank)
                .type("amazing_plays_win_with_one_hp")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("One HP matters")
                .displayName("Achievement completed: One HP matters (" + getLeagueText(minimumRank) + ")")
                .displayCardId("GILA_BOSS_49p")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with one HP left and no armor remaining in Ranked Standard")
                .completedText("You won with one HP left in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(HEALTH_AT_END).values(newArrayList("1")).build(),
                        Requirement.builder().type(ARMOR_AT_END).values(newArrayList("0")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildWinWithFullHps() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildWinWithFullHp(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildWinWithFullHp(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_win_with_full_hp_" + minimumRank)
                .type("amazing_plays_win_with_full_hp")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Don't Hit Me!")
                .displayName("Achievement completed: Don't Hit Me! (" + getLeagueText(minimumRank) + ")")
                .displayCardId("BRMA10_4")
                .displayCardType("minion")
                .difficulty("rare")
                .emptyText("Win one game with your full HP left in Ranked Standard")
                .completedText("You won with your full HP left in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(3)
                .points(5)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(DAMAGE_AT_END).values(newArrayList("0")).build(),
                        Requirement.builder().type(GAME_MIN_TURNS).values(newArrayList("4")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildGameTies() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildGameTie(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildGameTie(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_game_tie_" + minimumRank)
                .type("amazing_plays_game_tie")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("That's A Nice Tie")
                .displayName("Achievement completed: That's A Nice Tie (" + getLeagueText(minimumRank) + ")")
                .displayCardId("KAR_044a") // Moroes' stewart
                .displayCardType("minion")
                .difficulty("legendary")
                .emptyText("End the game with a tie in Ranked Standard")
                .completedText("You tied a game in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(null)
                .points(15 + (25 - minimumRank))
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_TIE).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildWinWithoutTakingDamages() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildWinWithoutTakingDamage(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildWinWithoutTakingDamage(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_win_without_taking_damage_" + minimumRank)
                .type("amazing_plays_win_without_taking_damage")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Immune")
                .displayName("Achievement completed: Immune (" + getLeagueText(minimumRank) + ")")
                .displayCardId("CRED_69")
                .displayCardType("minion")
                .difficulty("epic")
                .emptyText("Win the game without taking any damage in Ranked Standard")
                .completedText("You won a game without taking any damage in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(3)
                .points(10 + (25 - minimumRank))
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(GAME_WON).build(),
                        Requirement.builder().type(TOTAL_DAMAGE_TAKEN).values(newArrayList("0")).build(),
                        Requirement.builder().type(GAME_MIN_TURNS).values(newArrayList("4")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildDiscardCards() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildDiscardCard(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildDiscardCard(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_discard_cards_" + minimumRank)
                .type("amazing_plays_discard_cards")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Put it Away")
                .displayName("Achievement completed: Put it Away (" + getLeagueText(minimumRank) + ")")
                .displayCardId("TRL_252")
                .displayCardType("minion")
                .difficulty("epic")
                .emptyText("Discard 8 cards in a single game in Ranked Standard")
                .completedText("You discarded 8 cards in a single game in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank))
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_DISCARD).values(newArrayList("8")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildHighkeeperRas() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildHighkeeperRa(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildHighkeeperRa(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_summon_highkeeper_ra_" + minimumRank)
                .type("amazing_plays_summon_highkeeper_ra (" + getLeagueText(minimumRank) + ")")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Highkeeper Ra")
                .displayName("Achievement completed: Highkeeper Ra is among us! (" + getLeagueText(minimumRank) + ")")
                .displayCardId("ULD_705t")
                .displayCardType("minion")
                .difficulty("legendary")
                .emptyText("Summon Highkeeper Ra in Ranked Standard")
                .completedText("You summoned Highkeeper Ra in " + getLeagueText(minimumRank) + " or better")
                .maxNumberOfRecords(null)
                .points(40)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(MINION_SUMMONED).values(newArrayList("ULD_705t")).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> buildTotalHeals() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> buildTotalHeal(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement buildTotalHeal(int minimumRank, boolean isRoot) {
        int amountToHeal = 30;
        return RawAchievement.builder()
                .id("amazing_plays_hero_heal_" + minimumRank)
                .type("amazing_plays_hero_heal")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Healed!")
                .displayName("Achievement completed: Healed! (" + getLeagueText(minimumRank) + ")")
                .displayCardId("CS1h_001_H2_AT_132")
                .displayCardType("minion")
                .emptyText("Heal your hero for " + amountToHeal + " health in Ranked Standard")
                .completedText("You healed your hero for " + amountToHeal + " health in " + getLeagueText(minimumRank) + " or better")
                .difficulty("rare")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank) / 2)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_HERO_HEAL).values(newArrayList("" + amountToHeal, QUALIFIER_AT_LEAST)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> totalDamageInGames() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> totalDamageInGame(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement totalDamageInGame(int minimumRank, boolean isRoot) {
        int damageToDeal = 120;
        return RawAchievement.builder()
                .id("amazing_plays_deal_damage_" + minimumRank)
                .type("amazing_plays_deal_damage")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Burn Them All!")
                .displayName("Achievement completed: Burn Them All! (" + getLeagueText(minimumRank) + ")")
                .displayCardId("BRM_027p")
                .displayCardType("minion")
                .emptyText("Deal at least " + damageToDeal + " damage during one Ranked Standard game")
                .completedText("You dealt " + damageToDeal + " damage in " + getLeagueText(minimumRank) + " or better")
                .difficulty("rare")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank) / 2)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_DAMAGE_DEALT).values(newArrayList("" + damageToDeal, QUALIFIER_AT_LEAST)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> totalArmorInGames() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> totalArmorInGame(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement totalArmorInGame(int minimumRank, boolean isRoot) {
        int armorToGain = 60;
        return RawAchievement.builder()
                .id("amazing_plays_gain_armor_" + minimumRank)
                .type("amazing_plays_gain_armor")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Iron Defense")
                .displayName("Achievement completed: Iron Defense (" + getLeagueText(minimumRank) + ")")
                .displayCardId("EX1_606")
                .displayCardType("spell")
                .emptyText("Gain at least " + armorToGain + " armor during one Ranked Standard game")
                .completedText("You gained " + armorToGain + " armor in " + getLeagueText(minimumRank) + " or better")
                .difficulty("rare")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank) / 2)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(TOTAL_ARMOR_GAINED).values(newArrayList("" + armorToGain, QUALIFIER_AT_LEAST)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> desertObelisks() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> desertObelisk(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement desertObelisk(int minimumRank, boolean isRoot) {
        return RawAchievement.builder()
                .id("amazing_plays_desert_obelisk_" + minimumRank)
                .type("amazing_plays_desert_obelisk")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Desert Obelisks")
                .displayName("Achievement completed: Desert Obelisks (" + getLeagueText(minimumRank) + ")")
                .displayCardId("ULD_703")
                .displayCardType("minion")
                .emptyText("Control 3 Desert Obelisks during your turn in Ranked Standard")
                .completedText("You controlled 3 Desert Obelisks in " + getLeagueText(minimumRank) + " or better")
                .difficulty("epic")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank) / 2)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(MINIONS_CONTROLLED_DURING_TURN).values(newArrayList("ULD_703", "3", QUALIFIER_AT_LEAST)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private List<RawAchievement> recurringVillains() {
        List<Integer> minimumRanks = newArrayList(5, 4, 3, 2, 1);
        return minimumRanks.stream()
                .map(minimumRank -> recurringVillain(minimumRank, minimumRank == 5))
                .collect(Collectors.toList());
    }

    private RawAchievement recurringVillain(int minimumRank, boolean isRoot) {
        int numberOfResummons = 3;
        return RawAchievement.builder()
                .id("amazing_plays_recurring_villains_" + minimumRank)
                .type("amazing_plays_recurring_villains")
                .icon("boss_victory")
                .root(isRoot)
                .priority(-minimumRank)
                .name("Recurring Villains")
                .displayName("Achievement completed: Recurring Villains (" + getLeagueText(minimumRank) + ")")
                .displayCardId("DAL_749")
                .displayCardType("minion")
                .emptyText("Resummon a Recurring Villain at least " + numberOfResummons + " times in Ranked Standard")
                .completedText("You resummoned " + numberOfResummons + " Recurring Villains in " + getLeagueText(minimumRank) + " or better")
                .difficulty("rare")
                .maxNumberOfRecords(3)
                .points(5 + (25 - minimumRank) / 2)
                .requirements(newArrayList(
                        Requirement.builder().type(GAME_TYPE).values(newArrayList(RANKED)).build(),
                        Requirement.builder().type(RANKED_MIN_LEAGUE).values(newArrayList("" + minimumRank)).build(),
                        Requirement.builder().type(RANKED_FORMAT_TYPE).values(newArrayList(STANDARD)).build(),
                        Requirement.builder().type(RESUMMONED_RECURRING_VILLAIN).values(newArrayList("3", QUALIFIER_AT_LEAST)).build()
                ))
                .resetEvents(newArrayList(GameEvents.GAME_START))
                .build();
    }

    private String getLeagueText(int leagueId) {
        switch (leagueId) {
            case 5: return "Bronze";
            case 4: return "Silver";
            case 3: return "Gold";
            case 2: return "Platinum";
            case 1: return "Diamond";
        }
        return "Unknown";
    }
}
