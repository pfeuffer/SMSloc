package de.pfeufferweb.android.whereru;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
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
		sendSMS(receiver, getPosition());
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void sendSMS(String phoneNumber, String message) {
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				SmsReceiver.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
	}

	private String getPosition() {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			return String.format("Longitude: %1$s ; Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
		} else {
			return "unknown";
		}
	}
}
