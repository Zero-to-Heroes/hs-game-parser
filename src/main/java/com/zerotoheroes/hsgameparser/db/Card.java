package com.zerotoheroes.hsgameparser.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
// http://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

	private String id;
	private int cost;
	private String name;
	private String playerClass;
	private String type;
	private String rarity;
}
