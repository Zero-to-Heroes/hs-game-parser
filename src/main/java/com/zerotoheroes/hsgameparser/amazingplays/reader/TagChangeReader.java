package com.zerotoheroes.hsgameparser.amazingplays.reader;

import com.zerotoheroes.hsgameentities.enums.GameTag;
import com.zerotoheroes.hsgameentities.replaydata.GameHelper;
import com.zerotoheroes.hsgameentities.replaydata.entities.BaseEntity;
import com.zerotoheroes.hsgameentities.replaydata.gameactions.TagChange;
import com.zerotoheroes.hsgameparser.amazingplays.GameEvents;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TagChangeReader {

	private final GameEvents events;
	private final GameHelper helper;

	public void read(TagChange data) {

		// Start turn
		if (data.getEntity() == 1 && data.getName() == GameTag.TURN.getIntValue()) {
			events.turnStart(data.getValue());
		}

		// PREDAMAGE is how much damage should be applied. This includes damage
		// taken from the armor, while DAMAGE doesn't include armor loss
		if (data.getName() == GameTag.PREDAMAGE.getIntValue()) {
			if (data.getValue() > 0 && data.getEntity() != 1) {
				BaseEntity entity = helper.getEntity(data.getEntity());
				events.damageTaken(entity, data.getValue());
			}
		}

		// Some things still happen after the PLAYSTATE change to WON/LOST/TIED,
		// so we need to use this
		// Only raise a single gameEnd event
		if (data.getName() == GameTag.GOLD_REWARD_STATE.getIntValue() && data.getEntity() == 2) {
			events.gameEnd();
		}
	}
}
