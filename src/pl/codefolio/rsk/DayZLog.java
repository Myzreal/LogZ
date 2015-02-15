package pl.codefolio.rsk;

import java.util.ArrayList;
import java.util.HashMap;

import pl.codefolio.rsk.components.Player;
import pl.codefolio.rsk.events.Event;
import pl.codefolio.rsk.exceptions.InvalidDatetimeFormatException;

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
 * This is the output object of LogZ. Any information retrieved from the logs are contained
 * here.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class DayZLog {

	private HashMap<Long, Player> players = new HashMap<Long, Player>();	// Holds each player that appeared in the logs in any context - each player is stored only once.
	private ArrayList<Event> events = new ArrayList<Event>();	// Holds all the events in a chronological order.
	
	/**
	 * Adds a player to the list if he is not on it yet.
	 * If the player is already on the list then he is not added.
	 */
	public void add(Player player) {
		if (!players.containsKey(player.getId())) {
			players.put(player.getId(), player);
		}
	}
	
	/**
	 * Adds an event to the list.
	 */
	public void add(Event event) {
		events.add(event);
	}
	
	/**
	 * Retrieves a player from the list.
	 * 
	 * WARNING: This returns the object itself so be careful when modifying it.
	 * If you wish to retrieve a safe object then use getXXXCopy().
	 */
	public Player getPlayer(long id) {
		return players.get(id);
	}
	
	/**
	 * Retrieves a copy of the player from the list.
	 * 
	 * Since it's a copy it is safe to modify it without changing
	 * the DayZLog state.
	 */
	public Player getPlayerCopy(long id) {
		return players.get(id).copy();
	}
	
	/**
	 * Retrieves the list of players.
	 * 
	 * WARNING: This returns the object itself so be careful when modifying it.
	 * If you wish to retrieve a safe object then use getXXXCopy().
	 */
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> output = new ArrayList<Player>();
		for (Player p : players.values()) {
			output.add(p);
		}
		return output;
	}
	
	
	/**
	 * Retrieves a copy of the list of players.
	 * 
	 * Since it's a copy it is safe to modify it without changing
	 * the DayZLog state.
	 */
	public ArrayList<Player> getPlayersCopy() {
		ArrayList<Player> output = new ArrayList<Player>();
		for (Player p : players.values()) {
			output.add(p.copy());
		}
		return output;
	}
	
	/**
	 * Retrieves a copy of the list of events filtered accordingly.
	 * If you wish to get all the events then simply do not pass any argument.
	 * Otherwise only events that match the provided filter classes will be returned.
	 * 
	 * Since it's a copy it is safe to modify it without changing
	 * the DayZLog state.
	 */
	public ArrayList<Event> getEventsCopy(Class<? extends Event>... filter) throws InvalidDatetimeFormatException {
		ArrayList<Event> output = new ArrayList<Event>();
		for (Event event : events) {
			if (inFilter(filter, event)) {
				output.add(event.copy());
			}
		}
		
		return output;
	}
	
	
	/**
	 * Retrieves the list of events filtered accordingly.
	 * If you wish to get all the events then simply do not pass any argument.
	 * Otherwise only events that match the provided filter classes will be returned.
	 * 
	 * WARNING: This returns the object itself so be careful when modifying it.
	 * If you wish to retrieve a safe object then use getXXXCopy().
	 */
	public ArrayList<Event> getEvents(Class<? extends Event>... filter) throws InvalidDatetimeFormatException {
		ArrayList<Event> output = new ArrayList<Event>();
		for (Event event : events) {
			if (inFilter(filter, event)) {
				output.add(event);
			}
		}
		
		return output;
	}
	
	private boolean inFilter(Class<? extends Event>[] filter, Event element) {
		if (filter == null || filter.length == 0)
			return true;
		
		for (Class<? extends Event> c : filter) {
			if (element.getClass().equals(c))
				return true;
		}
		return false;
	}
}
