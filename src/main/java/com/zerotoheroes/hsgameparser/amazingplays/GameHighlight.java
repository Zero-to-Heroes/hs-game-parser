package com.zerotoheroes.hsgameparser.amazingplays;

import lombok.Data;

@Data
public class GameHighlight {
	private final int turn;
	private final String description;

	private String data;
}
