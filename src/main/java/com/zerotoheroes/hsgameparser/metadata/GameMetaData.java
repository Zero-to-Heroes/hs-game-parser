package com.zerotoheroes.hsgameparser.metadata;

import lombok.Data;

@Data
public class GameMetaData {
	private String playerName, opponentName;
	private String playerClass, opponentClass;

	private int numberOfTurns;
	private int durationInSeconds;
	private String winStatus;
	private String playCoin;
}
