package com.zerotoheroes.hsgameparser.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;

import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Getter
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

	public Card find(String cardId) {
		Optional<Card> optional = cards.stream().filter(c -> c.getId().equals(cardId)).findFirst();
		if (optional.isPresent()) { return optional.get(); }
		return null;
	}

	public Card fromDbfId(int dbfId) {
		Optional<Card> optional = cards.stream().filter(c -> c.getDbfId() == dbfId).findFirst();
		if (optional.isPresent()) { return optional.get(); }
		return null;
	}
}
