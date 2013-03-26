package de.pfeufferweb.android.whereru;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SendService extends Service {
	private final IBinder binder = new Binder();
	private final RequestHandler requestHandler = new RequestHandler(this);

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String receiver = intent.getExtras().getString("receiver");
		ActiveLocationRequest newRequestResult = requestHandler
				.newRequest(receiver);
		getPosition(newRequestResult);
		ListenActivityBroadcast.updateActivity(this);
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private void getPosition(final ActiveLocationRequest request) {
		new TimedLocationSender(this, request).start();
	}
}
