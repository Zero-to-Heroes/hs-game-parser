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

    public static List<String> STANDARD_SETS = Lists.newArrayList("core", "expert1", "gilneas", "boomsday",
            "troll", "dalaran", "uldum", "dragons", "yod");
    public static List<String> ARENA_SETS = Lists.newArrayList("core", "expert1", "brm", "tgt", "kara",
            "icecrown", "troll", "dragons", "yod");

    @Test
    public void generate_config() throws Exception {
        CardsList cardsList = CardsList.create();
        List<DbCard> allSecrets = cardsList.getDbCards().stream()
                .filter(card -> card.getMechanics() != null)
                .filter(card -> card.getMechanics().contains("SECRET"))
                .filter(card -> card.isCollectible())
                .collect(Collectors.toList());
        SecretsConfig wildSecrets = SecretsConfig.builder()
                .mode("wild")
                .secrets(allSecrets.stream()
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        SecretsConfig standardSecrets = SecretsConfig.builder()
                .mode("standard")
                .secrets(allSecrets.stream()
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
                        .filter(secret -> ARENA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        List<SecretsConfig> conf = Lists.newArrayList(wildSecrets, standardSecrets, arenaSecrets);
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