package de.pfeufferweb.android.whereru;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationSender {
	private final Context context;
	private final String receiver;
	private final int notificationId;

	public LocationSender(Context context, String receiver, int notificationId) {
		this.context = context;
		this.receiver = receiver;
		this.notificationId = notificationId;
	}

	public void send(Location location) {
		String text = format(location);
		Log.d("SendService", "text: " + text);
		// Toast.makeText(SendService.this, text, Toast.LENGTH_LONG).show();
		sendSMS(text, receiver, notificationId);
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
