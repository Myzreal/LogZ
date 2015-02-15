package pl.codefolio.rsk.tests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pl.codefolio.rsk.DayZLog;
import pl.codefolio.rsk.LogZ;
import pl.codefolio.rsk.components.Player;
import pl.codefolio.rsk.events.Event;
import pl.codefolio.rsk.events.KillEvent;
import pl.codefolio.rsk.exceptions.LogZException;

/**
 * --------------------------------------------------------------------
 * 
 *  LogZ - a parser for DayZ Standalone logs.
    Copyright (C) 2015  Radoslaw Skupnik
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * --------------------------------------------------------------------
 * 
 * @author Radoslaw Skupnik
 *
 */
public class ParseTest {
	
	DayZLog log;

	@Test
	public void basicTest() {
		try {
			log = LogZ.make("test.ADM", StandardCharsets.ISO_8859_1.name());
		} catch (LogZException | IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		ArrayList<Event> events = null;
		ArrayList<Player> players = null;
		try {
			events = log.getEvents(KillEvent.class);
		} catch (LogZException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		if (events != null) {
			Assert.assertEquals(events.size(), 95);
		} else {
			Assert.fail();
		}
		
		try {
			events = log.getEventsCopy(KillEvent.class);
		} catch (LogZException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		if (events != null) {
			Assert.assertEquals(events.size(), 95);
		} else {
			Assert.fail();
		}
		
		try {
			events = log.getEventsCopy();
		} catch (LogZException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		if (events != null) {
			Assert.assertEquals(events.size(), 4312);
		} else {
			Assert.fail();
		}
		
		
		players = log.getPlayersCopy();

		if (players != null) {
			Assert.assertEquals(players.size(), 560);
		} else {
			Assert.fail();
		}
	}
}
