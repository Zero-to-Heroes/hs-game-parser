package com.zerotoheroes.hsgameparser.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.Getter;

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
}
