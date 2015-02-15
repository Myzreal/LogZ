package pl.codefolio.rsk.tests;

import org.junit.Test;

import pl.codefolio.rsk.components.Datetime;
import pl.codefolio.rsk.exceptions.InvalidDatetimeFormatException;
import static org.junit.Assert.*;

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
public class DatetimeTest {

	@Test
	public void basicTest() {
		try {
			Datetime time = new Datetime("2015-02-11", "23:00:32", "23:41:18");
			assertEquals("2015-2-11 23:41:18", time.getDatetime());
			time = new Datetime("2015-02-11", "23:00:32", "00:00:10");
			assertEquals("2015-2-12 0:0:10", time.getDatetime());
			time = new Datetime("2015-02-28", "23:00:32", "00:00:10");
			assertEquals("2015-3-1 0:0:10", time.getDatetime());
			time = new Datetime("2015-02-12", "11:00:24", "00:00:10");
			assertEquals("2015-2-12 0:0:10", time.getDatetime());
		} catch (InvalidDatetimeFormatException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void correctedTest() {
		try {
			Datetime time = new Datetime("2015-02-11", "23:00:32", "23:41:18");
			assertEquals("2015-02-11 23:41:18", time.getDatetimeCorrected());
			time = new Datetime("2015-02-11", "23:00:32", "00:00:10");
			assertEquals("2015-02-12 00:00:10", time.getDatetimeCorrected());
			time = new Datetime("2015-02-28", "23:00:32", "00:00:10");
			assertEquals("2015-03-01 00:00:10", time.getDatetimeCorrected());
			time = new Datetime("2015-02-12", "11:00:24", "00:00:10");
			assertEquals("2015-02-12 00:00:10", time.getDatetimeCorrected());
		} catch (InvalidDatetimeFormatException e) {
			e.printStackTrace();
		}
	}
}
