package com.zerotoheroes.hsgameparser.db;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestCardManipulations {

    @Test
    public void generate_dungeon_run_progression() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("HERO_\\d{2}"))
                .collect(Collectors.toList());
        List<String> strings = heroes.stream()
                .flatMap(card -> {
                    List<String> allSteps = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        allSteps.add(buildDungeonRunProgressionStep(card, i));
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(strings.size() + " should be " + 8 * 9);
    }

    private String buildDungeonRunProgressionStep(DbCard card, int i) {
        String difficulty = "free";
        if (i == 7) {
            difficulty = "epic";
        }
        return "{ "
                + "\"type\": \"dungeon_run_progression\", "
                + "\"id\": \"dungeon_run_progression_" + card.getId() + "_" + i + "\", "
                + "\"cardId\": \"" + card.getId() + "\", "
                + "\"cardDbfId\": " + card.getDbfId() + ", "
                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                + "\"step\": " + i + ", "
                + "\"name\": \"" + card.getName() + " (" + card.getPlayerClass() + ") - Cleared round " + (i + 1) + "\", "
                + "\"difficulty\": \"" + difficulty + "\"}";
    }

    @Test
    public void generate_monster_hunt_progression() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> Arrays.asList("GILA_400h", "GILA_500h3", "GILA_600h", "GILA_900h").contains(card.getId()))
                .collect(Collectors.toList());
        System.out.println(heroes.stream().map(DbCard::getId).collect(Collectors.toList()));
        List<String> strings = heroes.stream()
                .flatMap(card -> {
                    List<String> allSteps = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        allSteps.add(buildMonsterHuntProgressionStep(card, i));
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        strings.add(generateMonsterHuntBossDefeat());
        System.out.println(String.join(",\n", strings));
        System.out.println(strings.size() + " should be " + (8 * 4 + 1));
    }

    private String generateMonsterHuntBossDefeat() {
        return "{ "
                + "\"type\": \"monster_hunt_progression\", "
                + "\"id\": \"monster_hunt_final_boss\", "
                + "\"cardId\": \"GILA_BOSS_61h\", "
                + "\"cardDbfId\": 48954, "
                + "\"name\": \"Defeated Hagatha the Witch!\", "
                + "\"difficulty\": \"legendary\"}";
    }

    private String buildMonsterHuntProgressionStep(DbCard card, int i) {
        String difficulty = "free";
        if (i == 7) {
            difficulty = "epic";
        }
        String afterText = "Cleared round " + (i + 1);
        if (i == 8) {
            afterText = "Defeated Hagatha!";
        }

        return "{ "
                + "\"type\": \"monster_hunt_progression\", "
                + "\"id\": \"monster_hunt_progression_" + card.getId() + "_" + i + "\", "
                + "\"cardId\": \"" + card.getId() + "\", "
                + "\"cardDbfId\": " + card.getDbfId() + ", "
                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                + "\"step\": " + i + ", "
                + "\"name\": \"" + card.getName() + " - " + afterText + "\", "
                + "\"difficulty\": \"" + difficulty + "\"}";
    }

    @Test
    public void generate_loota_boss() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("LOOTA_BOSS"))
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> strings = bossCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"dungeon_run_boss_encounter\", "
                        + "\"achievementId\": \"dungeon_run_boss_encounter_" + card.getId() + "\", "
                        + "\"bossId\": \"" + card.getId() + "\", "
                        + "\"bossDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": " + card.getName() + ", "
                        + "\"difficulty\": }")
                .collect(Collectors.toList());
        System.out.println(String.join("\n", strings));
    }

    @Test
    public void generate_dungeon_run_treasures() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("LOOTA_\\d{3}[a-z]?")
                        || card.getId().equals("LOOT_998l") // Wondrous wand
                        || card.getId().equals("LOOT_998k")) // Golden kobold
                .filter(card -> !card.getId().equals("LOOTA_842t")) // Forging of Quel'Delar
                .filter(card -> !Arrays.asList("LOOTA_102", "LOOTA_103", "LOOTA_104", "LOOTA_105", "LOOTA_107", "LOOTA_109")
                        .contains(card.getId())) // Minions summoned by bosses
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"dungeon_run_treasure_play\", "
                        + "\"id\": \"dungeon_run_treasure_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 32");
    }

    @Test
    public void generate_dungeon_run_passives() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("LOOTA_\\d{3}[a-z]?"))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"dungeon_run_passive_play\", "
                        + "\"id\": \"dungeon_run_passive_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 14");
    }

    @Test
    public void generate_monster_hunt_treasures() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("GILA_\\d{3}[a-z]?")
                        || Arrays.asList("GILA_BOSS_23t", "GILA_BOSS_29t", "GILA_BOSS_43t", "GILA_BOSS_37t",
                        "GILA_BOSS_26t2", "GILA_BOSS_33t", "GILA_BOSS_57t").contains(card.getId()))
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
                .filter(card -> !Arrays.asList("GILA_400t", "GILA_500t", "GILA_601", "GILA_604", "GILA_817t",
                        "GILA_818t", "GILA_819t", "GILA_825d").contains(card.getId()))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"monster_hunt_treasure_play\", "
                        + "\"id\": \"monster_hunt_treasure_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 53 (or 62?)");
    }

    @Test
    public void generate_monsetr_hunt_passives() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("GILA_\\d{3}[a-z]?")
                        || Arrays.asList("LOOTA_825", "LOOTA_803", "LOOTA_804", "LOOTA_800", "LOOTA_824", "LOOTA_831", "LOOTA_801").contains(card.getId()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> !card.getType().equals("Enchantment"))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"monster_hunt_passive_play\", "
                        + "\"id\": \"monster_hunt_passive_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 21");
    }

    @Test
    public void generate_gila_boss() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("GILA_BOSS"))
                .filter(card -> !card.getId().equals("GILA_BOSS_52h2")) // Beastly Pete
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> strings = bossCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"monster_hunt_boss_encounter\", "
                        + "\"id\": \"monster_hunt_boss_encounter_" + card.getId() + "\", "
                        + "\"bossId\": \"" + card.getId() + "\", "
                        + "\"bossDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"common\"}")
                .collect(Collectors.toList());
//        System.out.println(String.join("\n", bossCards.stream().map(DbCard::toString).collect(Collectors.toList())));
        System.out.println(String.join(",\n", strings));
    }

    @Test
    public void generate_trl_shrines() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> shrines = buildShrines(cardsList);
//        System.out.println(shrines.size() + " shrines");
        List<String> strings = shrines.stream()
                .map(card -> "{ "
                        + "\"type\": \"rumble_run_shrine_play\", "
                        + "\"id\": \"rumble_run_shrine_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"free\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
    }

    private List<DbCard> buildShrines(CardsList cardsList) {
        return cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_\\d{3}"))
                .filter(card -> "Minion".equals(card.getType()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("InvisibleDeathrattle"))
                .filter(card -> card.getCost() == 0)
                .filter(card -> card.getAttack() == 0)
                .filter(card -> card.getHealth() > 1)
                .collect(Collectors.toList());
    }

    @Test
    public void dalaran_heist_hunt_treasures() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
                        || Arrays.asList("").contains(card.getId()))
                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> "Minion".equals(card.getType())
                        || "Spell".equals(card.getType())
                        || "Weapon".equals(card.getType()))
                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
                .filter(card -> !card.getName().contains("Anomaly - "))
                .filter(card -> !Arrays.asList(
                        "DALA_501", // Cheerful Spirit?
                        "DALA_502", // Infuriated Banker
                        "DALA_503", "DALA_504", // Kirin Tor XXX
                        "DALA_714a", "DALA_714b", "DALA_714c", // Candles twinspell
                        "DALA_716t", // 50 dmg bomb token,
                        "DALA_724d",
                        "DALA_728d",
                        "DALA_744d",
                        "DALA_746d",
                        "DALA_800", "DALA_801", "DALA_802", "DALA_803", "DALA_804", "DALA_805", "DALA_806", "DALA_807", "DALA_808", // Random decks
                        "DALA_829", "DALA_829t", // Mage's starting power
                        "DALA_830", "DALA_831", "DALA_832", "DALA_832t", "DALA_833", "DALA_833t", // Some stuff to change the run contents.
                        "DALA_901", "DALA_902", "DALA_903", "DALA_904", "DALA_905", "DALA_906", "DALA_907", "DALA_908",
                            "DALA_909", "DALA_910", "DALA_911", "DALA_912", "DALA_913", "DALA_914" // Tavern stuff. Maybe handle them in another achievemnet?
                        ).contains(card.getId()))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"dalaran_heist_treasure_play\", "
                        + "\"id\": \"dalaran_heist_treasure_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 29 + ???");
    }

    @Test
    public void generate_dalaran_heist_passives() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> treasureCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
                        || Arrays.asList(
                                "GILA_506", // First Aid Kit
                                "LOOTA_803", // Scepter of Summoning
                                "LOOTA_832", // Cloak of Invisibility
                                "LOOTA_845" // Totem of the Dead
                            ).contains(card.getId()))
                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
                .filter(card -> !card.getType().equals("Enchantment"))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"dalaran_heist_passive_play\", "
                        + "\"id\": \"dalaran_heist_passive_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
        System.out.println(treasureCards.size() + " sould be 16 + ???");
    }

    @Test
    public void generate_dalaran_heist_bosses() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> bossCards = cardsList.getDbCards().stream()
                .filter(card -> card.getId().startsWith("DALA_BOSS"))
                .filter(card -> "Hero".equals(card.getType()))
                // Remove bartenders
                .filter(card -> !Lists.newArrayList("DALA_BOSS_98h", "DALA_BOSS_99h").contains(card.getId()))
                .collect(Collectors.toList());
        List<String> strings = bossCards.stream()
                .flatMap(card -> buildDalaranBossEntries(card).stream())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
    }

    private List<String> buildDalaranBossEntries(DbCard card) {
        return Lists.newArrayList(
                buildDalaranBossEntry(card, "dalaran_heist_boss_encounter"),
                buildDalaranBossEntry(card, "dalaran_heist_boss_victory"),
                buildDalaranBossEntry(card, "dalaran_heist_boss_encounter_heroic"),
                buildDalaranBossEntry(card, "dalaran_heist_boss_victory_heroic"));
    }

    private String buildDalaranBossEntry(DbCard card, String type) {
        return "{ "
                + "\"type\": \"" + type + "\", "
                + "\"id\": \"" + type + "_" + card.getId() + "\", "
                + "\"cardId\": \"" + card.getId() + "\", "
                + "\"cardDbfId\": " + card.getDbfId() + ", "
                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                + "\"name\": \"" + card.getName().replace("\"", "") + "\", "
                + "\"difficulty\": \"rare\"}";
    }

//    @Test
//    public void generate_dalaran_heist_progression() throws Exception {
//        // Hero (9) - Hero Power (3) - Difficulty (2) - Step (8)
//        CardsList cardsList = CardsList.create();
//        List<Pair<String, List<String>>> heroesAndPowers = Lists.newArrayList(
//                new Pair<>("DALA_Rakanishu", Lists.newArrayList("CS2_034", "DALA_Mage_HP1", "DALA_Mage_HP1"))
//        );
//        List<Pair<String, String>> flattedHeroPowers = heroesAndPowers.stream()
//                .flatMap(pair -> pair.getValue().stream().map(heroPower -> new Pair<>(pair.getKey(), heroPower)))
//                .collect(Collectors.toList());
//        List<Tuple> flatProgressionAchievements = flattedHeroPowers.stream()
//                .flatMap(hp -> Lists.newArrayList(
//                        new Tuple(hp.getKey(), hp.getValue(), "normal"),
//                        new Tuple(hp.getKey(), hp.getValue(), "heroic"))
//                        .stream())
//                .flatMap(hp -> IntStream.range(0, 8).mapToObj(i -> { hp.addData(i); return Tuple.tuple(hp.) hp; }))
//                .collect(Collectors.toList());
//        List<String> strings = flatProgressionAchievements.stream()
//                .map(achv -> "{ "
//                        + "\"type\": \"dalaran_heist_progression\", "
//                        + "\"id\": \"dalaran_heist_passive_progression_" + buildAchievementId(achv) + "\", "
//                        + "\"cardId\": \"" + achv.toList().get(0) + "\", "
//                        + "\"secondaryCardId\": " + achv.toList().get(1) + "\", "
//                        + "\"name\": \"" + buildAchievementName(cardsList, achv) + "\", "
//                        + "\"difficulty\": \"rare\"}")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//        System.out.println(flatProgressionAchievements.size() + " should be 432");
//    }

    private String buildAchievementId(Tuple achv) {
        return achv.toList().stream().map(Object::toString).collect(Collectors.joining("_"));
    }

    private String buildAchievementName(CardsList cardsList, Tuple achv) {
        return achv.toList().stream()
                .map(Object::toString)
                .map(id -> cardsList.findDbCard(id) != null ? cardsList.findDbCard(id).getName() : id)
                .collect(Collectors.joining(" "));
    }

//    @Test
//    public void generate_trl_heroes() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> heroes = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().matches("TRLA_209h_[a-zA-Z]+"))
//                .collect(Collectors.toList());
//        List<String> strings = heroes.stream()
//                .flatMap(card -> {
//                    List<String> allSteps = new ArrayList<>();
//                    for (int i = 0; i < 8; i++) {
//                        allSteps.add(buildRumbleProgressionStep(card, i));
//                    }
//                    return allSteps.stream();
//                })
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }

    @Test
    public void generate_trl_teammates() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> shrines = buildShrines(cardsList);
        List<DbCard> teammates = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
                .filter(card -> !shrines.contains(card))
                .filter(card -> !card.getId().equals("TRLA_107")) // Tribal Shrine
                .filter(card -> card.getType().equals("Minion"))
                .collect(Collectors.toList());
        List<String> strings = teammates.stream()
                .map(card -> "{ "
                        + "\"type\": \"rumble_run_teammate_play\", "
                        + "\"id\": \"rumble_run_teammate_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
    }

    @Test
    public void generate_trl_passives() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> passives = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
                .filter(card -> card.getType().equals("Spell"))
                .collect(Collectors.toList());
        List<String> strings = passives.stream()
                .map(card -> "{ "
                        + "\"type\": \"rumble_run_passive_play\", "
                        + "\"id\": \"rumble_run_passive_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"rare\"}")
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
    }

    private String buildRumbleProgressionStep(DbCard card, int i) {
        String difficulty = "free";
        if (i == 7) {
            difficulty = "epic";
        }
        return "{ "
                + "\"type\": \"rumble_run_progression\", "
                + "\"id\": \"rumble_run_progression_" + card.getId() + "_" + i + "\", "
                + "\"cardId\": \"" + card.getId() + "\", "
                + "\"cardDbfId\": " + card.getDbfId() + ", "
                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
                + "\"step\": " + i + ", "
                + "\"name\": \"" + card.getName() + " (" + card.getPlayerClass() + ") - Cleared round " + (i + 1) + "\", "
                + "\"difficulty\": \"" + difficulty + "\"}";
    }

    @Test
    public void generate_all_spells() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> spellCards = cardsList.getDbCards().stream()
                .filter(DbCard::isCollectible)
                .filter(card -> "Spell".equalsIgnoreCase(card.getType()))
                .collect(Collectors.toList());
        String csvSpellList = spellCards.stream()
                .sorted(Comparator.comparing(DbCard::getSet).thenComparing(DbCard::getId))
                .map(card -> String.join(",", card.getSet(), card.getId(), card.getType(), card.getName(), ""))
                .collect(Collectors.joining("\n"));
        csvSpellList = "set,cardId,type,name,soundRecorded?\n" + csvSpellList;
        System.out.println(csvSpellList);
        System.out.println(spellCards.size() + " spell");
    }
}
