package com.zerotoheroes.hsgameparser;

import com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import lombok.Builder;
import lombok.Getter;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class SecretsConfigGenerator implements WithAssertions {

    public static List<String> STANDARD_SETS = Lists.newArrayList("core", "expert1", "dalaran", "uldum", "dragons",
            "yod", "black_temple", "scholomance", "darkmoon_faire", "darkmoon_races");
    public static List<String> VANILLA_SETS = Lists.newArrayList("vanilla");
    public static List<String> ARENA_SETS = Lists.newArrayList("darkmoon_races", "darkmoon_faire", "scholomance",
            "black_temple", "boomsday", "ungoro", "kara", "core", "expert1");

    @Test
    public void generate_config() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> allSecrets = cardsList.getDbCards().stream()
                .filter(card -> card.getMechanics() != null)
                .filter(card -> card.getMechanics().contains("SECRET"))
                .filter(card -> card.isCollectible())
                .collect(Collectors.toList());
        SecretsConfig duelsSecrets = SecretsConfig.builder()
                .mode("duels")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        SecretsConfig vanillaSecrets = SecretsConfig.builder()
                .mode("classic")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        SecretsConfig wildSecrets = SecretsConfig.builder()
                .mode("wild")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        SecretsConfig standardSecrets = SecretsConfig.builder()
                .mode("standard")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .filter(secret -> STANDARD_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        SecretsConfig arenaSecrets = SecretsConfig.builder()
                .mode("arena")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .filter(secret -> ARENA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        List<SecretsConfig> conf = Lists.newArrayList(wildSecrets, standardSecrets, vanillaSecrets, arenaSecrets, duelsSecrets);
        System.out.println(GeneralHelper.serialize(conf));
    }

    @Test
    public void test() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> allSecrets = cardsList.getDbCards().stream()
                .filter(card -> card.getMechanics() != null)
                .filter(card -> card.getMechanics().contains("SECRET"))
                .filter(card -> card.isCollectible())
                .collect(Collectors.toList());
        SecretsConfig wildSecrets = SecretsConfig.builder()
                .mode("wild")
                .secrets(allSecrets.stream()
                        .filter(secret -> secret.getPlayerClass().toLowerCase().equals("rogue"))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .cardName(secret.getName())
                                .cardText(secret.getText())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        List<SecretsConfig> conf = Lists.newArrayList(wildSecrets);
        System.out.println(GeneralHelper.serialize(conf));


    }
}

@Builder
@Getter
class SecretsConfig {
    private final String mode;
    private final List<SecretConfig> secrets;
}

@Builder
@Getter
class SecretConfig {
    private final String cardId;
    private final String cardName;
    private final String cardText;
    private final String playerClass;
}