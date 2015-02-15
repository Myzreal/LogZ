package pl.codefolio.rsk.events;

import pl.codefolio.rsk.components.Datetime;
import pl.codefolio.rsk.components.Player;
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
 * This event is constructed out of the kill lines.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class KillEvent extends Event {

	protected Player killer, victim;
	
	public KillEvent(Datetime datetime, Player killer, Player victim) {
		super(datetime);
		this.killer = killer;
		this.victim = victim;
	}
	
	public KillEvent copy() throws InvalidDatetimeFormatException {
		return new KillEvent(datetime.copy(), killer.copy(), victim.copy());
	}

	public Player getKiller() {
		return killer;
	}
	
	public Player getVictim() {
		return victim;
	}
	
	@Override
	public String toString() {
		return "[type: Kill; datetime: "+datetime.getDatetimeCorrected()+"; killer: "+killer+"; victim: "+victim+"]";
	}
}
