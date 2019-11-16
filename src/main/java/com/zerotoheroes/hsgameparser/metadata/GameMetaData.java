package com.zerotoheroes.hsgameparser.metadata;

import lombok.Data;

@Data
public class GameMetaData {
	private String playerName, opponentName;
	private String playerClass, opponentClass;
	// For AI encounters
	private String playerCardId, opponentCardId;

	private int numberOfTurns;
	private int durationInSeconds;
	private String result;
	private String additionalResult;
	private String playCoin;
	private String skillLevel;
}
