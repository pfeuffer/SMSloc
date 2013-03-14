package de.pfeufferweb.android.whereru;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
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
		updateNotification(context, receiver, message, notificationId);
	}

	private String format(Location location) {
		return (location != null) ? String
				.format("http://maps.google.de/maps?q=%1$s,%2$s ; Accuracy: %3$s m ; Speed: %4$s m/s ; Age: %5$s s",
						location.getLatitude(), location.getLongitude(),
						location.getAccuracy(), location.getSpeed(),
						getAge(location))
				: "unknown";
	}

	private long getAge(Location location) {
		return (System.currentTimeMillis() - location.getTime()) / 1000;
	}

	private void updateNotification(Context context, String phoneNumber,
			String message, int notificationId) {
		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle("request for position")
				.setContentText("position sent to " + phoneNumber)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								Intent.ACTION_VIEW), 0)).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}
}
