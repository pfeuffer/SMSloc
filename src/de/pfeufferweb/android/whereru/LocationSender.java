package de.pfeufferweb.android.whereru;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.telephony.SmsManager;

public class LocationSender {
	private final Context context;

	public LocationSender(Context context) {
		this.context = context;
	}

	public void send(Location location, String phoneNumber) {
		sendSMS(format(location), phoneNumber);
	}

	public void sendNoLocation(String phoneNumber) {
		sendSMS(context.getString(R.string.locationUnknown), phoneNumber);
	}

	private void sendSMS(String message, final String receiver) {
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(
				context, SmsReceiver.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(receiver, null, message, pi, null);
	}

	private String format(Location location) {
		return context.getString(R.string.locationResponse,
				location.getLatitude(), location.getLongitude(),
				location.getAccuracy(), location.getSpeed(), getAge(location));
	}

	private long getAge(Location location) {
		return (System.currentTimeMillis() - location.getTime()) / 1000;
	}

}
