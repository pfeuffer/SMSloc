package de.pfeufferweb.android.whereru;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import de.pfeufferweb.android.whereru.repository.LocationRequest;
import de.pfeufferweb.android.whereru.repository.RequestRepository;
import de.pfeufferweb.android.whereru.repository.SimpleLocation;

public class LocationSender {
	private final Context context;
	private final LocationRequest request;
	private final int notificationId;

	public LocationSender(Context context, LocationRequest request,
			int notificationId) {
		this.context = context;
		this.request = request;
		this.notificationId = notificationId;
	}

	public void send(Location location) {
		if (location == null) {
			request.setNoLocation();
			Log.d("SendService", "set request to no location");
		} else {
			request.setSuccess(new SimpleLocation(location.getLongitude(),
					location.getLatitude()));
			Log.d("SendService", "set request to success");
		}
		String text = format(location);
		Log.d("SendService", "text: " + text);
		sendSMS(text, request.getRequester(), notificationId);
		RequestRepository db = new RequestRepository(context);
		db.open();
		db.updateRequest(request);
		db.close();
		sendBroadcast();
	}

	private void sendBroadcast() {
		Intent intent = new Intent(ListenActivity.BROADCAST_NEW_REQUEST);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	private void sendSMS(String message, final String receiver,
			final int notificationId) {
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(
				context, SmsReceiver.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(receiver, null, message, pi, null);
		new Notifications().updateNotification(context, receiver, message,
				notificationId);
	}

	private String format(Location location) {
		return (location != null) ? context.getString(
				R.string.locationResponse, location.getLatitude(),
				location.getLongitude(), location.getAccuracy(),
				location.getSpeed(), getAge(location)) : context
				.getString(R.string.locationUnknown);
	}

	private long getAge(Location location) {
		return (System.currentTimeMillis() - location.getTime()) / 1000;
	}

}
