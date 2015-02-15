package pl.codefolio.rsk.parse;

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
 * Deals with pattern matching.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class Pattern {

	/**
	 * Matches the provided line to a pattern and returns it.
	 * @return a LinePattern of the specified line.
	 */
	public static LinePattern match(String line) {
		if (line.contains("AdminLog started on"))
			return LinePattern.LOG_START;
		else if (line.contains("| Player")) {
			if (line.contains(" is connected ("))
				return LinePattern.CONNECTED;
			else if (line.contains(") has been disconnected"))
				return LinePattern.DISCONNECTED;
			else if (line.contains(") has been killed by player "))
				return LinePattern.KILLED;
		} else if (line.contains("| Chat")) {
			return LinePattern.CHAT;
		}
		
		return LinePattern.UNKNOWN;
	}
}
