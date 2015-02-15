package pl.codefolio.rsk.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import pl.codefolio.rsk.DayZLog;
import pl.codefolio.rsk.LogZ;
import pl.codefolio.rsk.events.Event;
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
public class LogZExample {

	public static void main(String[] args) {
		try {
			DayZLog log = LogZ.make("test.ADM", StandardCharsets.UTF_8.name());	// You need to provide a test.ADM file in the same folder as LogZ library. A sample test.ADM is provided if you wish to use it (extract it from the jar).
			ArrayList<Event> events = log.getEventsCopy();
			for (Event e : events) {
				System.out.println(e);
			}
		} catch (LogZException | IOException e) {
			e.printStackTrace();
		}
	}
}
