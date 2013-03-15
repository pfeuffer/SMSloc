package de.pfeufferweb.android.whereru;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notifications {
	int notificationCounter = 0;

	public int setNotification(Context context, String origin) {
		int notificationId = ++notificationCounter;
		Notification notification = buildNotification(context,
				context.getString(R.string.notificationRequest, origin));
		notify(context, notificationId, notification);
		return notificationId;
	}

	public void updateNotification(Context context, String phoneNumber,
			String message, int notificationId) {
		Notification notification = buildNotification(context,
				context.getString(R.string.notificationSent, phoneNumber));
		notify(context, notificationId, notification);
	}

	private Notification buildNotification(Context context, String text) {
		return new NotificationCompat.Builder(context)
				.setContentTitle(context.getString(R.string.notificationTitle))
				.setContentText(text)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								context, ListenActivity.class), 0)).build();
	}

	private void notify(Context context, int notificationId,
			Notification notification) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}
}
