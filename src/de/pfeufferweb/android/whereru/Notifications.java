package de.pfeufferweb.android.whereru;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notifications {
	private final Context context;
	int notificationCounter = 0;

	public Notifications(Context context) {
		this.context = context;
	}

	public int newRequest(String origin) {
		int notificationId = ++notificationCounter;
		Notification notification = buildNotification(context.getString(
				R.string.notificationRequest, origin));
		notify(notificationId, notification);
		return notificationId;
	}

	public void success(String phoneNumber, int notificationId) {
		Notification notification = buildNotification(context.getString(
				R.string.notificationSentSuccess, phoneNumber));
		notify(notificationId, notification);
	}

	public void noFix(String phoneNumber, int notificationId) {
		Notification notification = buildNotification(context.getString(
				R.string.notificationSentNoFix, phoneNumber));
		notify(notificationId, notification);
	}

	public void noGps(String phoneNumber, int notificationId) {
		Notification notification = buildNotification(context.getString(
				R.string.notificationNoGps, phoneNumber));
		notify(notificationId, notification);
	}

	public void aborted(String phoneNumber, int notificationId) {
		Notification notification = buildNotification(context.getString(
				R.string.notificationAborted, phoneNumber));
		notify(notificationId, notification);
	}

	private Notification buildNotification(String text) {
		return new NotificationCompat.Builder(context)
				.setContentTitle(context.getString(R.string.notificationTitle))
				.setContentText(text)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								context, ListenActivity.class), 0)).build();
	}

	private void notify(int notificationId, Notification notification) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}
}
