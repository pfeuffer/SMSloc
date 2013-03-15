package de.pfeufferweb.android.whereru;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SendService extends Service {
	private final IBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {
		SendService getService() {
			return SendService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("SendService", "startet");
		final String receiver = intent.getExtras().getString("receiver");
		final int notificationId = intent.getExtras().getInt("notificationId");
		final int seconds = Times.values()[intent.getExtras().getInt("seconds")].seconds;
		getPosition(receiver, seconds, notificationId);
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private void getPosition(final String receiver, final int seconds,
			int notificationId) {
		new TimedLocationSender(this, receiver, seconds, notificationId)
				.start();
	}
}
