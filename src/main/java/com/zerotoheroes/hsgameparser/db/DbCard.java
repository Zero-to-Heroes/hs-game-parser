package com.zerotoheroes.hsgameparser.db;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
// http://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbCard {

	private String id;
	private int dbfId;
	private int cost;
	private String name;
	private int health;
	private String playerClass;
	private String type;
	private String rarity;
	private String set;
	private boolean collectible;

}
