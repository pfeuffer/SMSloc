package de.pfeufferweb.android.whereru;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	int notificationCounter = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		String requestText = Settings.getRequestText(context);
		boolean active = Settings.getActive(context);

		Log.d("SmsReceiver", "request text: " + requestText + "; active: "
				+ active);

		Bundle bundle = intent.getExtras();
		if (active && bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String origin = msg.getOriginatingAddress();
				String message = msg.getMessageBody().toString().trim();
				if (message.equals(requestText)) {
					int notificationId = setNotification(context, origin);
					Toast.makeText(context,
							context.getString(R.string.toastRequest, origin),
							Toast.LENGTH_LONG).show();
					Intent startService = new Intent(context, SendService.class);
					startService.putExtra("receiver", origin);
					startService.putExtra("notificationId", notificationId);
					context.startService(startService);
				}
			}
		}
	}

	private int setNotification(Context context, String origin) {
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
}
