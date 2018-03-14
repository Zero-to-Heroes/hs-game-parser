package com.zerotoheroes.hsgameparser.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.json.simple.parser.JSONParser;

public class CardsList {

	private List<Card> cards = new ArrayList<>();

	public static CardsList create() throws Exception {
		CardsList instance = new CardsList();
		JSONParser parser = new JSONParser();
		Reader reader = new BufferedReader(
				new InputStreamReader(CardsList.class.getResourceAsStream("cards.json"), "UTF-8"));
		String cardsString = ((org.json.simple.JSONArray) parser.parse(reader)).toJSONString();

		ObjectMapper mapper = new ObjectMapper();

		instance.cards = mapper.readValue(cardsString,
				TypeFactory.defaultInstance().constructCollectionType(List.class, Card.class));

		return instance;
	}

	public List<Card> getCards() {
		return cards;
	}

	public Card find(String cardId) {
		return cards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElse(null);
	}

	public Card fromDbfId(int dbfId) {
		return cards.stream().filter(c -> c.getDbfId() == dbfId).findFirst().orElse(null);
	}
}
