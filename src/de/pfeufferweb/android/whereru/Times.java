package de.pfeufferweb.android.whereru;

public enum Times {
	SECONDS_15(15), SECONDS_30(30), MINUTES_1(60), MINUTES_2(120), MINUTES_5(
			300), MINUTES_10(600), INFINITE(Integer.MAX_VALUE);

	public final int seconds;

	Times(int seconds) {
		this.seconds = seconds;
	}
}
