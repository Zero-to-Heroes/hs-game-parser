package com.zerotoheroes.hsgameparser;

import com.zerotoheroes.hsgameparser.achievements.generator.GeneralHelper;
import com.zerotoheroes.hsgameparser.db.CardsList;
import com.zerotoheroes.hsgameparser.db.DbCard;
import lombok.Builder;
import lombok.Getter;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SecretsConfigGenerator implements WithAssertions {

    public static List<String> STANDARD_SETS = Lists.newArrayList(
            "core",
            "scholomance",
            "darkmoon_faire",
            "darkmoon_races",
            "black_temple",
            "the_barrens");
    public static List<String> VANILLA_SETS = Lists.newArrayList("vanilla");
    public static List<String> ARENA_SETS = Lists.newArrayList(
            "the_barrens",
            "darkmoon_races",
            "darkmoon_faire",
            "scholomance",
            "black_temple",
            "dalaran",
            "gangs",
            "core");

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
                .secrets(removeDuplicates(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList())))
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
                .secrets(removeDuplicates(allSecrets.stream()
                        .filter(secret -> secret.getSet() != null)
                        .filter(secret -> !VANILLA_SETS.contains(secret.getSet().toLowerCase()))
                        .map(secret -> SecretConfig.builder()
                                .cardId(secret.getId())
                                .playerClass(secret.getPlayerClass().toLowerCase())
                                .build())
                        .collect(Collectors.toList())))
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

    // Not a real remove duplicates function, but for now the only known dupes are between Core and Legacy
	// and we can expect that, in the future, only the CORE set will have duplicates from other sets
	// Let's just hope they keep the card ID nomenclature consistent :)
	private List<SecretConfig> removeDuplicates(List<SecretConfig> secrets) {
    	List<String> secretIds = secrets.stream().map(SecretConfig::getCardId).collect(Collectors.toList());
    	return secrets.stream()
			    .filter(secret -> !secretIds.contains("CORE_" + secret.getCardId()))
			    .collect(Collectors.toList());
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