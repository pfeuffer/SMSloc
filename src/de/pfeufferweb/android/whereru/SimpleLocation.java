package de.pfeufferweb.android.whereru;

public class SimpleLocation {
	private final double longitude;
	private final double latitude;

	public SimpleLocation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
}
