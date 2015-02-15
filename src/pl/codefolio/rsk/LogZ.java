package pl.codefolio.rsk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import pl.codefolio.rsk.components.Datetime;
import pl.codefolio.rsk.components.Player;
import pl.codefolio.rsk.events.ChatEvent;
import pl.codefolio.rsk.events.KillEvent;
import pl.codefolio.rsk.events.PlayerConnectedEvent;
import pl.codefolio.rsk.events.PlayerDisconnectedEvent;
import pl.codefolio.rsk.exceptions.InvalidDatetimeFormatException;
import pl.codefolio.rsk.exceptions.LogZException;
import pl.codefolio.rsk.exceptions.ParseException;
import pl.codefolio.rsk.parse.LinePattern;
import pl.codefolio.rsk.parse.ParseUtils;
import pl.codefolio.rsk.parse.Pattern;

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
 * Provides functions to parse DayZ Standalone logs.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class LogZ {
	
	private static Datetime currentDatetime;	// Used to determine proper date and time of each entry.

	/**
	 * Parses a log passed as a large String object and return it as a DayZLog object.
	 * You must take care of the encoding prior to passing the String to this function.
	 * @param log - the log file as a large String
	 * @return the DayZLog object that contains the wrapped logs
	 * @throws LogZException if something goes wrong
	 */
	public static DayZLog make(String log) throws LogZException {
		return parse(log);
	}
	
	/**
	 * Parses a log passed as a relative path to a file.
	 * See the example to see how this is done.
	 * @param path - relative path to a DayZ log file (.ADM)
	 * @param encoding - encoding to be used, most commonly "UTF-8"
	 * @return the DayZLog object that contains the wrapped logs
	 * @throws LogZException if something goes wrong when parsing the logs
	 * @throws IOException if there is a problem with loading the file
	 */
	public static DayZLog make(String path, String encoding) throws LogZException, IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		String logString = new String(encoded, encoding);
		return parse(logString);
	}
	
	/**
	 * Parses a log passed as a File object.
	 * @param file - a File object constructed of the log (.ADM) - this func doesn't check if the passed file is correct.
	 * @param encoding - encoding to be used, most commonly "UTF-8"
	 * @return the DayZLog object that contains the wrapped logs
	 * @throws LogZException if something goes wrong when parsing the logs
	 * @throws IOException if there is a problem with loading the file
	 */
	public static DayZLog make(File file, String encoding) throws LogZException, IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		String logString = new String(encoded, encoding);
		return parse(logString);
	}
	
	/**
	 * Parses the log String and returns it as a DayZLog object.
	 */
	private static DayZLog parse(String log) throws LogZException {
		DayZLog output = new DayZLog();
		
		Scanner scanner = new Scanner(log);
		int i = 1;
		while (scanner.hasNextLine()) {
			interpret(output, scanner.nextLine(), i);
			i++;
		}
		scanner.close();
		
		return output;
	}
	
	/**
	 * Interprets a single line of the log.
	 */
	private static void interpret(DayZLog log, String line, int lineIndex) throws ParseException, InvalidDatetimeFormatException {
		if (line == null || line.length() == 0 || line.equals("") || line.contains("****"))		// Ignore those.
			return;
		
		LinePattern pattern = Pattern.match(line);	// Pattern matching figures out what type of log line are we dealing with.
		String[] temp = null;
		switch (pattern) {
			default:
				break;
			case LOG_START:
				temp = line.split(" ");
				currentDatetime = new Datetime(temp[3], temp[5], temp[5]);	// Used in determining the proper datetime for other events.
				break;
			case CONNECTED:
				temp = line.split(" ");
				if (temp.length < 7)
					throw new ParseException("Line of type "+pattern+" should have at least 7 elements", lineIndex);
				
				// Get the name.
				String name = ParseUtils.retrieveName(temp, 3, "is", "connected");
				name = name.substring(1, name.length()-1);
				
				// Get the id.
				String id = temp[temp.length-1];
				id = id.substring(4, id.length()-1);
				if (id.equals("Unknown"))
					break;
				
				// Retrieve the player.
				Player player = ParseUtils.retrievePlayer(id, name, log, lineIndex);
				
				// Retrieve the date and time.
				Datetime datetime = ParseUtils.retrieveDatetime(temp, 0, currentDatetime, lineIndex);
				
				// Register the event.
				log.add(new PlayerConnectedEvent(datetime, player));
				break;
			case DISCONNECTED:
				temp = line.split(" ");
				if (temp.length < 7)
					throw new ParseException("Line of type "+pattern+" should have at least 7 elements", lineIndex);
				
				// Get the name.
				name = ParseUtils.retrieveName(temp, 3, "has", "been");
				String[] nameTemp = name.split("id=");
				name = nameTemp[0].substring(1, nameTemp[0].length()-2);
				
				// Get the id.
				id = nameTemp[1].substring(0, nameTemp[1].length()-1);
				if (id.equals("Unknown"))
					break;
				
				// Retrieve the player.
				player = ParseUtils.retrievePlayer(id, name, log, lineIndex);
				
				// Retrieve date and time.
				datetime = ParseUtils.retrieveDatetime(temp, 0, currentDatetime, lineIndex);
				
				// Register the event.
				log.add(new PlayerDisconnectedEvent(datetime, player));
				break;
			case KILLED:
				temp = line.split(" ");
				if (temp.length < 10)
					throw new ParseException("Line of type "+pattern+" should have at least 10 elements", lineIndex);
				
				// Get the killer!
				// Start with his name.
				name = ParseUtils.retrieveName(temp, 3, "has", "been");
				nameTemp = name.split("id=");
				name = nameTemp[0].substring(1, nameTemp[0].length()-2);
				
				// Now get his id.
				id = nameTemp[1].substring(0, nameTemp[1].length()-1);
				if (id.equals("Unknown"))
					break;
				
				// Recognize the vile killer!
				Player killer = ParseUtils.retrievePlayer(id, name, log, lineIndex);
				
				// Now let's find the victim.
				// To get the name we need to first find out where the killer name ends.
				int phraseLoc = ParseUtils.locatePhrase(temp, lineIndex, "has", "been", "killed", "by", "player");
				// Now the name.
				name = ParseUtils.retrieveName(temp, phraseLoc+5, null, null);	// TODO: HARDCODED OFFSET
				nameTemp = name.split("id=");
				name = nameTemp[0].substring(1, nameTemp[0].length()-2);
				
				// Now the victim's id.
				id = nameTemp[1].substring(0, nameTemp[1].length()-1);
				if (id.equals("Unknown"))
					break;
				
				// Identify the victim.
				Player victim = ParseUtils.retrievePlayer(id, name, log, lineIndex);
				
				// Now establish the time of murder.
				datetime = ParseUtils.retrieveDatetime(temp, 0, currentDatetime, lineIndex);
				
				// Register the kill.
				log.add(new KillEvent(datetime, killer, victim));
				
				break;
			case CHAT:
				temp = line.split(":");
				if (temp.length < 3)
					throw new ParseException("Line of type "+pattern+" should have at least 3 elements", lineIndex);
				
				// Extract the name.
				nameTemp = temp[2].split(" ");
				StringBuilder nameBuilder = new StringBuilder();
				for (int i = 2; i < nameTemp.length; i++) {
					if (i == 2)
						nameBuilder.append(nameTemp[i]);
					else
						nameBuilder.append(" "+nameTemp[i]);
				}
				name = nameBuilder.toString();
				nameTemp = name.split("\"");
				name = nameTemp[1];
				
				// Now the id.
				id = nameTemp[2].substring(4, nameTemp[2].length()-2);
				if (id.equals("Unknown"))
					break;
				
				// Construct the player.
				player = ParseUtils.retrievePlayer(id, name, log, lineIndex);
				
				// Retrieve chat text.
				String text = temp[temp.length-1];
				text = text.substring(1);
				
				// Retrieve date and time.
				datetime = ParseUtils.retrieveDatetime(line.split(" "), 0, currentDatetime, lineIndex);
				
				// Register the event.
				log.add(new ChatEvent(datetime, player, text));
				break;
			case UNKNOWN:
				break;
		}
	}
}
