package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class TimedLocationProvider implements LocationListener {

	private static final int MAX_AGE = 60000;

	private final LocationManager locationManager;
	private final long maxWaitTime;
	private final long acceptedAccuracy;
	private Location bestLocation;

	public TimedLocationProvider(Context context, long maxWaitTime,
			long acceptedAccuracy) {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		this.maxWaitTime = maxWaitTime;
		this.acceptedAccuracy = acceptedAccuracy;
		bestLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 1, this);
	}

	public synchronized Location getBestLocation() {
		long startTime = System.currentTimeMillis();
		Log.d("TimedLocationProvider", "entering loop: "
				+ !locationGoodEnough(startTime));
		while (!locationGoodEnough(startTime)) {
			Log.d("TimedLocationProvider", "position not good enough; waiting");
			try {
				this.wait(maxWaitTime);
			} catch (InterruptedException e) {
				Log.e("TimedLocationProvider", "exception in wait", e);
			}
		}
		locationManager.removeUpdates(this);
		return bestLocation;
	}

	private long timeWaited(long startTime) {
		return System.currentTimeMillis() - startTime;
	}

	private boolean locationGoodEnough(long startTime) {
		return bestLocation != null && !tooOld()
				&& bestLocation.getAccuracy() <= acceptedAccuracy
				|| timeWaited(startTime) > maxWaitTime;
	}

	private boolean tooOld() {
		long age = System.currentTimeMillis() - bestLocation.getTime();
		Log.d("TimeLocationProvider", "age: " + age);
		boolean tooOld = age > MAX_AGE;
		Log.d("TimeLocationProvider", "tooOld: " + tooOld);
		return tooOld;
	}

	@Override
	public void onLocationChanged(Location actLocation) {
		if (bestLocation == null
				|| actLocation.getAccuracy() < bestLocation.getAccuracy()) {
			bestLocation = actLocation;
			this.notify();
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}
}
