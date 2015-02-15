package pl.codefolio.rsk.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * A useful class that deal with all the date and time problems that often appear in logs.
 * It makes sure the date and time are appropriate even if the log started shortly before midnight.
 * 
 * @author Radoslaw Skupnik
 *
 */
public class Datetime {
	
	private int year, month, day, hour, minute, second;

	/**
	 * @param dayString - yyyy-MM-dd
	 * @param dayStartTime - time of log start in HH:mm:ss format
	 * @param time - time of the specific event in HH:mm:ss format
	 * @throws InvalidDatetimeFormatException if something goes wrong when calculating the date and time
	 */
	public Datetime(String dayString, String dayStartTime, String time) throws InvalidDatetimeFormatException {
		String[] dayTemp = dayString.split("-");
		if (dayTemp == null || dayTemp.length != 3 || dayTemp[0].length() != 4 ||
			dayTemp[1].length() != 2 || dayTemp[2].length() != 2)
			throw new InvalidDatetimeFormatException(dayString);
		
		try {
			year = Integer.parseInt(dayTemp[0]);
			month = Integer.parseInt(dayTemp[1]);
			day = Integer.parseInt(dayTemp[2]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new InvalidDatetimeFormatException(dayString);
		}
		
		String[] timeTemp = time.split(":");
		if (timeTemp == null || timeTemp.length != 3 || timeTemp[0].length() != 2 ||
			timeTemp[1].length() != 2 || timeTemp[2].length() != 2)
			throw new InvalidDatetimeFormatException(time);
		
		try {
			hour = Integer.parseInt(timeTemp[0]);
			minute = Integer.parseInt(timeTemp[1]);
			second = Integer.parseInt(timeTemp[2]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new InvalidDatetimeFormatException(time);
		}
		
		if (hour >= 0 && hour <= 5) {
			timeTemp = dayStartTime.split(":");
			if (timeTemp == null || timeTemp.length != 3 || timeTemp[0].length() != 2 ||
				timeTemp[1].length() != 2 || timeTemp[2].length() != 2)
				throw new InvalidDatetimeFormatException(dayStartTime);
			
			int tHour = 0;
			
			try {
				tHour = Integer.parseInt(timeTemp[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new InvalidDatetimeFormatException(dayStartTime);
			}
			
			if (tHour <= 23 && tHour > 20) {
				incrementDate();
			}
		}
	}
	
	/**
	 * Increments the date by one day.
	 * This is required in some cases when events happen after midnight.
	 */
	public void incrementDate() throws InvalidDatetimeFormatException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateInString = day+"-"+month+"-"+year;
		try {
	 
			Date date = formatter.parse(dateInString);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DAY_OF_MONTH, 1);
			date = c.getTime();
			String newDate = formatter.format(date);
			String[] temp = newDate.split("-");
			if (temp == null || temp.length != 3)
				throw new InvalidDatetimeFormatException(newDate);
			
			try {
				day = Integer.parseInt(temp[0]);
				month = Integer.parseInt(temp[1]);
				year = Integer.parseInt(temp[2]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new InvalidDatetimeFormatException(newDate);
			}
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return a copy of this object.
	 */
	public Datetime copy() throws InvalidDatetimeFormatException {
		return new Datetime(getDateString(), getTimeString(), getTimeString());
	}
	
	/**
	 * @deprecated use getDatetimeCorrected() instead
	 * @return datetime as a String
	 */
	@Deprecated
	public String getDatetime() {
		return ""+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	}
	
	/**
	 * @return date and time as a properly formatted string (yyyy-MM-dd HH:mm:ss)
	 */
	public String getDatetimeCorrected() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInString = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter.format(date);
	}
	
	/**
	 * @return date as a properly formatted string (yyyy-MM-dd)
	 */
	public String getDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = year+"-"+month+"-"+day;
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter.format(date);
	}
	
	/**
	 * @return time as a properly formatted string (HH:mm:ss)
	 */
	public String getTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateInString = hour+":"+minute+":"+second;
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter.format(date);
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int getSecond() {
		return second;
	}
}
