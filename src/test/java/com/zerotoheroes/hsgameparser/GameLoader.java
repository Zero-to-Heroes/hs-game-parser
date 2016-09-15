package com.zerotoheroes.hsgameparser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.zerotoheroes.hsgameconverter.ReplayConverter;
import com.zerotoheroes.hsgameentities.replaydata.HearthstoneReplay;

public class GameLoader {

	@SuppressWarnings("resource")
	public HearthstoneReplay load(String xmlName) throws Exception {
		String xml = new Scanner(getClass().getResourceAsStream("replayxml/" + xmlName), "UTF-8").useDelimiter("\\A")
				.next();
		InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		ReplayConverter converter = new ReplayConverter();
		HearthstoneReplay replay = converter.replayFromXml(stream);
		return replay;
	}

}
