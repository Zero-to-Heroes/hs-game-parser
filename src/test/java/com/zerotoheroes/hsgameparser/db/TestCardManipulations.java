package com.zerotoheroes.hsgameparser.db;

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
                        "GILA_818t", "GILA_819t", "GILA_854t").contains(card.getId()))
                .collect(Collectors.toList());
        List<String> strings = treasureCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"monster_hunt_treasure_play\", "
                        + "\"id\": \"monster_hunt_treasure_play_" + card.getId() + "\", "
                        + "\"cardId\": \"" + card.getId() + "\", "
                        + "\"cardDbfId\": " + card.getDbfId() + ", "
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
                .filter(card -> "Hero".equals(card.getType()))
                .collect(Collectors.toList());
        List<String> strings = bossCards.stream()
                .map(card -> "{ "
                        + "\"type\": \"monster_hunt_boss_encounter\", "
                        + "\"achievementId\": \"monster_hunt_boss_encounter_" + card.getId() + "\", "
                        + "\"bossId\": \"" + card.getId() + "\", "
                        + "\"bossDbfId\": " + card.getDbfId() + ", "
                        + "\"name\": \"" + card.getName() + "\", "
                        + "\"difficulty\": \"common\"}")
                .collect(Collectors.toList());
//        System.out.println(String.join("\n", bossCards.stream().map(DbCard::toString).collect(Collectors.toList())));
        System.out.println(String.join("\n", strings));
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
    public void generate_trl_heroes() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> heroes = cardsList.getDbCards().stream()
                .filter(card -> card.getId().matches("TRLA_209h_[a-zA-Z]+"))
                .collect(Collectors.toList());
        List<String> strings = heroes.stream()
                .flatMap(card -> {
                    List<String> allSteps = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        allSteps.add(buildRumbleProgressionStep(card, i));
                    }
                    return allSteps.stream();
                })
                .collect(Collectors.toList());
        System.out.println(String.join(",\n", strings));
    }

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
