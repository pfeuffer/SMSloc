package de.pfeufferweb.android.whereru;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.pfeufferweb.android.whereru.repository.LocationRequest;
import de.pfeufferweb.android.whereru.repository.RequestRepository;

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
		final int seconds = intent.getExtras().getInt("seconds");
		LocationRequest request = writeRequest(receiver);
		getPosition(request, seconds, notificationId);
		ListenActivityBroadcast.updateActivity(this);
		return START_NOT_STICKY;
	}

	private LocationRequest writeRequest(final String receiver) {
		return new RequestRepository(this).createRequest(receiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private void getPosition(final LocationRequest request, final int seconds,
			int notificationId) {
		new TimedLocationSender(this, request, seconds, notificationId).start();
	}
}
