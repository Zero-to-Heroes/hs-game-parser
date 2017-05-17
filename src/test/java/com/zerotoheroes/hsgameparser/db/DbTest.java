package com.zerotoheroes.hsgameparser.db;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class DbTest implements WithAssertions {

	@Test
	public void testLoadFromDbfId() throws Exception {
		CardsList cardsList = CardsList.create();
		Card card = cardsList.fromDbfId(31);

		assertThat(card).isNotNull();
		assertThat(card.getId()).isEqualTo("HERO_05");
	}
}
