package de.pfeufferweb.android.whereru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	private final Notifications notifications = new Notifications();

	@Override
	public void onReceive(Context context, Intent intent) {
		String requestText = Settings.getRequestText(context);
		boolean active = Settings.getActive(context);
		int seconds = Settings.getSeconds(context);

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
					int notificationId = notifications.setNotification(context,
							origin);
					Toast.makeText(context,
							context.getString(R.string.toastRequest, origin),
							Toast.LENGTH_LONG).show();
					Intent startService = new Intent(context, SendService.class);
					startService.putExtra("receiver", origin);
					startService.putExtra("notificationId", notificationId);
					startService.putExtra("seconds", seconds);
					context.startService(startService);
				}
			}
		}
	}

}
