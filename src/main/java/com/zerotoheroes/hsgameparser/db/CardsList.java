package com.zerotoheroes.hsgameparser.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CardsList implements ICardsList {

	private List<DbCard> dbCards = new ArrayList<>();

	public static CardsList create() throws Exception {
		CardsList instance = new CardsList();
		JSONParser parser = new JSONParser();
		Reader reader = new BufferedReader(
				new InputStreamReader(CardsList.class.getResourceAsStream("cards.json"), "UTF-8"));
		String cardsString = ((org.json.simple.JSONArray) parser.parse(reader)).toJSONString();

		ObjectMapper mapper = new ObjectMapper();

		instance.dbCards = mapper.readValue(cardsString,
				TypeFactory.defaultInstance().constructCollectionType(List.class, DbCard.class));

		return instance;
	}

	@Override
	public List<DbCard> getDbCards() {
		return dbCards;
	}

	@Override
	public DbCard findDbCard(String cardId) {
		DbCard dbCard = dbCards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElse(null);
		return dbCard;
	}

	@Override
	public DbCard dbCardFromDbfId(int dbfId) {
		return dbCards.stream().filter(c -> c.getDbfId() == dbfId).findFirst().orElse(null);
	}
}
