package pl.codefolio.rsk.components;

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
 * Wraps the player data in a single object.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class Player {

	private String name;
	private long id;
	
	public Player(String name, long id) {
		this.name = name;
		this.id = id;
	}
	
	public Player copy() {
		return new Player(name, id);
	}
	
	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "[name: "+name+"; id: "+id+"]";
	}
}
