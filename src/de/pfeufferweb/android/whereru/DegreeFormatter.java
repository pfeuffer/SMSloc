package de.pfeufferweb.android.whereru;

import java.util.Locale;

public class DegreeFormatter {

	public String format(double number) {
		int integerValue = (int) number;
		double minutes = Math.abs(number - integerValue) * 60;
		int minutesIntegerValue = (int) minutes;
		double seconds = (minutes - minutesIntegerValue) * 60;

		return String.format(Locale.ENGLISH, "%d°+%d'+%.2f\"", integerValue,
				minutesIntegerValue, seconds);
	}
}
