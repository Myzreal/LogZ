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
 * This event is constructed out of the player connection lines.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class PlayerConnectedEvent extends Event {
	
	private Player player;

	public PlayerConnectedEvent(Datetime datetime, Player player) {
		super(datetime);
		this.player = player;
	}
	
	public PlayerConnectedEvent copy() throws InvalidDatetimeFormatException {
		return new PlayerConnectedEvent(datetime.copy(), player.copy());
	}

	public Player getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		return "[type: PlayerConnected; datetime: "+datetime.getDatetimeCorrected()+"; player: "+player+"]";
	}
}
