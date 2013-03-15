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
		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle(context.getString(R.string.notificationTitle))
				.setContentInfo(
						context.getString(R.string.notificationRequest, origin))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								Intent.ACTION_VIEW), 0)).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int notificationId = ++notificationCounter;
		notificationManager.notify(notificationId, notification);
		return notificationId;
	}

	public void updateNotification(Context context, String phoneNumber,
			String message, int notificationId) {
		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle(context.getString(R.string.notificationTitle))
				.setContentText(
						context.getString(R.string.notificationSent,
								phoneNumber))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								Intent.ACTION_VIEW), 0)).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}
}
