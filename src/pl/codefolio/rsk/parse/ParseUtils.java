package pl.codefolio.rsk.parse;

import pl.codefolio.rsk.DayZLog;
import pl.codefolio.rsk.components.Datetime;
import pl.codefolio.rsk.components.Player;
import pl.codefolio.rsk.exceptions.InvalidDatetimeFormatException;
import pl.codefolio.rsk.exceptions.ParseException;

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
 * A utility class with some functions useful when parsing logs.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class ParseUtils {

	/**
	 * Retrieves a name from the line.
	 * @param line - the line separated by " "
	 * @param startIndex - index of where the name start
	 * @param followingWordOne - first of the two following words that are used to check for name end (can be null)
	 * @param followingWordTwo - second of the two following words that are used to check for name end (can be null)
	 * @return the name which may require additional formatting
	 */
	public static String retrieveName(String[] line, int startIndex, String followingWordOne, String followingWordTwo) {
		StringBuilder nameBuilder = new StringBuilder(line[startIndex]);
		for (int i = startIndex+1; i < line.length; i++) {
			if (followingWordOne != null && followingWordTwo != null) {		// If those are specified then we need to check for 'em.
				if (line[i].equals(followingWordOne) && line[i+1].equals(followingWordTwo))
					break;
				else
					nameBuilder.append(" "+line[i]);
			} else {	// If following words are not specified then we just read till the end.
				nameBuilder.append(" "+line[i]);
			}
		}
		return nameBuilder.toString();
	}
	
	/**
	 * Constructs the player object, adds it if not yet added and returns it.
	 */
	public static Player retrievePlayer(String id, String name, DayZLog log, int li) throws ParseException {
		long idlong = 0;
		try {
			idlong = Long.parseLong(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ParseException("Invalid id: "+id, li);
		}
		
		Player player = log.getPlayer(idlong);
		if (player == null) {
			player = new Player(name, idlong);
			log.add(player);
		}
		
		return player;
	}
	
	/**
	 * Retrieves the date and time from the line
	 * @param line - separated by " "
	 * @param index - index where the time is located in the line
	 * @param current - the current Datetime object from LogZ
	 * @param li - line index
	 * @return a Datetime object
	 */
	public static Datetime retrieveDatetime(String[] line, int index, Datetime current, int li) throws ParseException, InvalidDatetimeFormatException {
		String time = line[index];
		String[] timeTemp = time.split(":");
		if (timeTemp.length < 3)
			throw new ParseException("Line should start with time in the HH:mm:ss format", li);
		return new Datetime(current.getDateString(), current.getTimeString(), time);
	}
	
	/**
	 * Locates the specified phrase in the provided line.
	 */
	public static int locatePhrase(String[] line, int li, String... phrase) throws ParseException {
		if (phrase == null || phrase.length == 0 || line == null || line.length == 0)
			throw new ParseException("Trying to find a phrase in a line where either the line or the phrase is empty", li);
		
		for (int i = 0; i < line.length; i++) {
			String word = line[i];
			if (word.equals(phrase[0])) {	// Locate the first word in the phrase.
				if (phrase.length == 1)		// If the phrase consists of one word then out work is done.
					return i;
				else {
					boolean isThisIt = true;
					for (int j = 1; j < phrase.length; j++) {	// Let's check the other words in the phrase - we can skip the first one.
						String phraseWord = phrase[j];
						if (!phraseWord.equals(line[i+j])) {	// This ain't the phrase we're looking for.
							isThisIt = false;
							break;
						}
					}
					if (isThisIt)	// So did we find it?
						return i;
				}
			}
		}
		
		return -1;
	}
}
