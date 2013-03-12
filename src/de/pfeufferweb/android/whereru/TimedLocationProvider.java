package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class TimedLocationProvider extends Thread implements LocationListener {

	private final LocationManager locationManager;
	private Location lastLocation;

	public TimedLocationProvider(Context context) {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void run() {
		this.requestPosition();
	}

	public Location getBestLocation() {
		return lastLocation;
	}

	protected Location requestPosition() {
		Log.d("TimedLocationProvider", "looper prepare");
		Looper.prepare();
		Log.d("TimedLocationProvider", "requesting location updates");
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this,
				null);
		Log.d("TimedLocationProvider", "looper loop");
		Looper.loop();
		return lastLocation;
	}

	@Override
	public void onLocationChanged(Location actLocation) {
		Log.d("TimedLocationProvider", "got location update");
		lastLocation = actLocation;
		locationManager.removeUpdates(this);
		Log.d("TimedLocationProvider", "looper quit");
		Looper.myLooper().quit();
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
