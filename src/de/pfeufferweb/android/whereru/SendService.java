package de.pfeufferweb.android.whereru;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class SendService extends Service {
	private LocationManager locationManager;
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		SendService getService() {
			return SendService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String receiver = intent.getExtras().getString("receiver");
		getPosition(receiver);
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void getPosition(final String receiver) {
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
				new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					@Override
					public void onProviderEnabled(String provider) {
					}

					@Override
					public void onProviderDisabled(String provider) {
					}

					@Override
					public void onLocationChanged(Location location) {
						String text = (location != null) ? String.format(
								"Latitude: %1$s ; Longitude: %2$s",
								location.getLatitude(), location.getLongitude())
								: "unknown";
						sendSMS(receiver, text);
					}

					private void sendSMS(String phoneNumber, String message) {
						PendingIntent pi = PendingIntent
								.getActivity(SendService.this, 0, new Intent(
										SendService.this, SmsReceiver.class), 0);
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(phoneNumber, null, message, pi,
								null);
					}
				}, null);
	}
}
