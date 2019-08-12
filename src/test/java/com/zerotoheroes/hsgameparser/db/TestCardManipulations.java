package com.zerotoheroes.hsgameparser.db;

public class TestCardManipulations {

//    @Test
//    public void dalaran_heist_hunt_treasures() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> treasureCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
//                        || Arrays.asList("").contains(card.getId()))
//                .filter(card -> card.getMechanics() == null || !card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
//                .filter(card -> "Minion".equals(card.getType())
//                        || "Spell".equals(card.getType())
//                        || "Weapon".equals(card.getType()))
//                .filter(card -> card.getText() == null || !card.getText().contains("Add to your deck"))
//                .filter(card -> !card.getName().contains("Anomaly - "))
//                .filter(card -> !Arrays.asList(
//                        "DALA_501", // Cheerful Spirit?
//                        "DALA_502", // Infuriated Banker
//                        "DALA_503", "DALA_504", // Kirin Tor XXX
//                        "DALA_714a", "DALA_714b", "DALA_714c", // Candles twinspell
//                        "DALA_716t", // 50 dmg bomb token,
//                        "DALA_724d",
//                        "DALA_728d",
//                        "DALA_744d",
//                        "DALA_746d",
//                        "DALA_800", "DALA_801", "DALA_802", "DALA_803", "DALA_804", "DALA_805", "DALA_806", "DALA_807", "DALA_808", // Random decks
//                        "DALA_829", "DALA_829t", // Mage's starting power
//                        "DALA_830", "DALA_831", "DALA_832", "DALA_832t", "DALA_833", "DALA_833t", // Some stuff to change the run contents.
//                        "DALA_901", "DALA_902", "DALA_903", "DALA_904", "DALA_905", "DALA_906", "DALA_907", "DALA_908",
//                            "DALA_909", "DALA_910", "DALA_911", "DALA_912", "DALA_913", "DALA_914" // Tavern stuff. Maybe handle them in another achievemnet?
//                        ).contains(card.getId()))
//                .collect(Collectors.toList());
//        List<String> strings = treasureCards.stream()
//                .map(card -> "{ "
//                        + "\"type\": \"dalaran_heist_treasure_play\", "
//                        + "\"id\": \"dalaran_heist_treasure_play_" + card.getId() + "\", "
//                        + "\"cardId\": \"" + card.getId() + "\", "
//                        + "\"cardDbfId\": " + card.getDbfId() + ", "
//                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                        + "\"name\": \"" + card.getName() + "\", "
//                        + "\"difficulty\": \"rare\"}")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//        System.out.println(treasureCards.size() + " sould be 29 + ???");
//    }
//
//    @Test
//    public void generate_dalaran_heist_passives() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> treasureCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().matches("DALA_\\d{3}[a-z]?")
//                        || Arrays.asList(
//                                "GILA_506", // First Aid Kit
//                                "LOOTA_803", // Scepter of Summoning
//                                "LOOTA_832", // Cloak of Invisibility
//                                "LOOTA_845" // Totem of the Dead
//                            ).contains(card.getId()))
//                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
//                .filter(card -> !card.getType().equals("Enchantment"))
//                .collect(Collectors.toList());
//        List<String> strings = treasureCards.stream()
//                .map(card -> "{ "
//                        + "\"type\": \"dalaran_heist_passive_play\", "
//                        + "\"id\": \"dalaran_heist_passive_play_" + card.getId() + "\", "
//                        + "\"cardId\": \"" + card.getId() + "\", "
//                        + "\"cardDbfId\": " + card.getDbfId() + ", "
//                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                        + "\"name\": \"" + card.getName() + "\", "
//                        + "\"difficulty\": \"rare\"}")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//        System.out.println(treasureCards.size() + " sould be 16 + ???");
//    }
//
//    @Test
//    public void generate_dalaran_heist_bosses() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> bossCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().startsWith("DALA_BOSS"))
//                .filter(card -> "Hero".equals(card.getType()))
//                // Remove bartenders
//                .filter(card -> !Lists.newArrayList("DALA_BOSS_98h", "DALA_BOSS_99h").contains(card.getId()))
//                .collect(Collectors.toList());
//        List<String> strings = bossCards.stream()
//                .flatMap(card -> buildDalaranBossEntries(card).stream())
//                .sorted(Comparator.naturalOrder())
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }
//
//    private List<String> buildDalaranBossEntries(DbCard card) {
//        return Lists.newArrayList(
//                buildDalaranBossEntry(card, "dalaran_heist_boss_encounter"),
//                buildDalaranBossEntry(card, "dalaran_heist_boss_victory"),
//                buildDalaranBossEntry(card, "dalaran_heist_boss_encounter_heroic"),
//                buildDalaranBossEntry(card, "dalaran_heist_boss_victory_heroic"));
//    }
//
//    private String buildDalaranBossEntry(DbCard card, String type) {
//        return "{ "
//                + "\"type\": \"" + type + "\", "
//                + "\"id\": \"" + type + "_" + card.getId() + "\", "
//                + "\"cardId\": \"" + card.getId() + "\", "
//                + "\"cardDbfId\": " + card.getDbfId() + ", "
//                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                + "\"name\": \"" + card.getName().replace("\"", "") + "\", "
//                + "\"difficulty\": \"rare\"}";
//    }
//
////    @Test
////    public void generate_dalaran_heist_progression() throws Exception {
////        // Hero (9) - Hero Power (3) - Difficulty (2) - Step (8)
////        CardsList cardsList = CardsList.create();
////        List<Pair<String, List<String>>> heroesAndPowers = Lists.newArrayList(
////                new Pair<>("DALA_Rakanishu", Lists.newArrayList("CS2_034", "DALA_Mage_HP1", "DALA_Mage_HP1"))
////        );
////        List<Pair<String, String>> flattedHeroPowers = heroesAndPowers.stream()
////                .flatMap(pair -> pair.getValue().stream().map(heroPower -> new Pair<>(pair.getKey(), heroPower)))
////                .collect(Collectors.toList());
////        List<Tuple> flatProgressionAchievements = flattedHeroPowers.stream()
////                .flatMap(hp -> Lists.newArrayList(
////                        new Tuple(hp.getKey(), hp.getValue(), "normal"),
////                        new Tuple(hp.getKey(), hp.getValue(), "heroic"))
////                        .stream())
////                .flatMap(hp -> IntStream.range(0, 8).mapToObj(i -> { hp.addData(i); return Tuple.tuple(hp.) hp; }))
////                .collect(Collectors.toList());
////        List<String> strings = flatProgressionAchievements.stream()
////                .map(achv -> "{ "
////                        + "\"type\": \"dalaran_heist_progression\", "
////                        + "\"id\": \"dalaran_heist_passive_progression_" + buildAchievementId(achv) + "\", "
////                        + "\"cardId\": \"" + achv.toList().get(0) + "\", "
////                        + "\"secondaryCardId\": " + achv.toList().get(1) + "\", "
////                        + "\"name\": \"" + buildAchievementName(cardsList, achv) + "\", "
////                        + "\"difficulty\": \"rare\"}")
////                .collect(Collectors.toList());
////        System.out.println(String.join(",\n", strings));
////        System.out.println(flatProgressionAchievements.size() + " should be 432");
////    }
//
//    private String buildAchievementId(Tuple achv) {
//        return achv.toList().stream().map(Object::toString).collect(Collectors.joining("_"));
//    }
//
//    private String buildAchievementName(CardsList cardsList, Tuple achv) {
//        return achv.toList().stream()
//                .map(Object::toString)
//                .map(id -> cardsList.findDbCard(id) != null ? cardsList.findDbCard(id).getName() : id)
//                .collect(Collectors.joining(" "));
//    }
//
////    @Test
////    public void generate_trl_heroes() throws Exception {
////        CardsList cardsList = CardsList.create();
////        List<DbCard> heroes = cardsList.getDbCards().stream()
////                .filter(card -> card.getId().matches("TRLA_209h_[a-zA-Z]+"))
////                .collect(Collectors.toList());
////        List<String> strings = heroes.stream()
////                .flatMap(card -> {
////                    List<String> allSteps = new ArrayList<>();
////                    for (int i = 0; i < 8; i++) {
////                        allSteps.add(buildRumbleProgressionStep(card, i));
////                    }
////                    return allSteps.stream();
////                })
//
////                .collect(Collectors.toList());
////        System.out.println(String.join(",\n", strings));
////    }
//
//    @Test
//    public void generate_trl_teammates() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> shrines = buildShrines(cardsList);
//        List<DbCard> teammates = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
//                .filter(card -> !shrines.contains(card))
//                .filter(card -> !card.getId().equals("TRLA_107")) // Tribal Shrine
//                .filter(card -> card.getType().equals("Minion"))
//                .collect(Collectors.toList());
//        List<String> strings = teammates.stream()
//                .map(card -> "{ "
//                        + "\"type\": \"rumble_run_teammate_play\", "
//                        + "\"id\": \"rumble_run_teammate_play_" + card.getId() + "\", "
//                        + "\"cardId\": \"" + card.getId() + "\", "
//                        + "\"cardDbfId\": " + card.getDbfId() + ", "
//                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                        + "\"name\": \"" + card.getName() + "\", "
//                        + "\"difficulty\": \"rare\"}")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }
//
//    @Test
//    public void generate_trl_passives() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> passives = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().matches("TRLA_[0-9]+"))
//                .filter(card -> card.getType().equals("Spell"))
//                .collect(Collectors.toList());
//        List<String> strings = passives.stream()
//                .map(card -> "{ "
//                        + "\"type\": \"rumble_run_passive_play\", "
//                        + "\"id\": \"rumble_run_passive_play_" + card.getId() + "\", "
//                        + "\"cardId\": \"" + card.getId() + "\", "
//                        + "\"cardDbfId\": " + card.getDbfId() + ", "
//                        + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                        + "\"name\": \"" + card.getName() + "\", "
//                        + "\"difficulty\": \"rare\"}")
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }
//
//    private String buildRumbleProgressionStep(DbCard card, int i) {
//        String difficulty = "free";
//        if (i == 7) {
//            difficulty = "epic";
//        }
//        return "{ "
//                + "\"type\": \"rumble_run_progression\", "
//                + "\"id\": \"rumble_run_progression_" + card.getId() + "_" + i + "\", "
//                + "\"cardId\": \"" + card.getId() + "\", "
//                + "\"cardDbfId\": " + card.getDbfId() + ", "
//                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                + "\"step\": " + i + ", "
//                + "\"name\": \"" + card.getName() + " (" + card.getPlayerClass() + ") - Cleared round " + (i + 1) + "\", "
//                + "\"difficulty\": \"" + difficulty + "\"}";
//    }
//
//    @Test
//    public void generate_all_spells() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> spellCards = cardsList.getDbCards().stream()
//                .filter(DbCard::isCollectible)
//                .filter(card -> "Spell".equalsIgnoreCase(card.getType()))
//                .collect(Collectors.toList());
//        String csvSpellList = spellCards.stream()
//                .sorted(Comparator.comparing(DbCard::getSet).thenComparing(DbCard::getId))
//                .map(card -> String.join(",", card.getSet(), card.getId(), card.getType(), card.getName(), ""))
//                .collect(Collectors.joining("\n"));
//        csvSpellList = "set,cardId,type,name,soundRecorded?\n" + csvSpellList;
//        System.out.println(csvSpellList);
//        System.out.println(spellCards.size() + " spell");
//    }
//
//    @Test
//    public void generate_brm_crash_bosses() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> bossCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().startsWith("TB_EVILBRM"))
//                .filter(card -> "Hero".equals(card.getType()))
//                // Remove bartenders
//                .filter(card -> !Lists.newArrayList("TB_EVILBRM_BoomH", "TB_EVILBRM_HagathaH", "TB_EVILBRM_TogwaggleH").contains(card.getId()))
//                .collect(Collectors.toList());
//        List<String> strings = bossCards.stream()
//                .flatMap(card -> buildBrmCrashWeek1BossEntries(card).stream())
//                .sorted(Comparator.naturalOrder())
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }
//
//    private List<String> buildBrmCrashWeek1BossEntries(DbCard card) {
//        return Lists.newArrayList(
//                buildBrmCrashWeek1Entry(card, "brm_crash_week_1_boss_encounter"),
//                buildBrmCrashWeek1Entry(card, "brm_crash_week_1_boss_victory"));
//    }
//
//    @Test
//    public void generate_brm_crash_passives() throws Exception {
//        CardsList cardsList = CardsList.create();
//        List<DbCard> bossCards = cardsList.getDbCards().stream()
//                .filter(card -> card.getId().startsWith("TB_EVILBRM"))
//                .filter(card -> card.getMechanics() != null && card.getMechanics().contains("DUNGEON_PASSIVE_BUFF"))
//                // Remove bartenders
//                .filter(card -> !Lists.newArrayList("TB_EVILBRM_BoomH", "TB_EVILBRM_HagathaH", "TB_EVILBRM_TogwaggleH").contains(card.getId()))
//                .collect(Collectors.toList());
//        List<String> strings = bossCards.stream()
//                .flatMap(card -> buildBrmCrashWeek1PassiveEntries(card).stream())
//                .sorted(Comparator.naturalOrder())
//                .collect(Collectors.toList());
//        System.out.println(String.join(",\n", strings));
//    }
//
//    private List<String> buildBrmCrashWeek1PassiveEntries(DbCard card) {
//        return Lists.newArrayList(
//                buildBrmCrashWeek1Entry(card, "brm_crash_week_1_passive_play"));
//    }
//
//    private String buildBrmCrashWeek1Entry(DbCard card, String type) {
//        return "{ "
//                + "\"type\": \"" + type + "\", "
//                + "\"id\": \"" + type + "_" + card.getId() + "\", "
//                + "\"cardId\": \"" + card.getId() + "\", "
//                + "\"cardDbfId\": " + card.getDbfId() + ", "
//                + "\"cardType\": \"" + card.getType().toLowerCase() + "\", "
//                + "\"name\": \"" + card.getName().replace("\"", "") + "\", "
//                + "\"difficulty\": \"rare\"}";
//    }
}
