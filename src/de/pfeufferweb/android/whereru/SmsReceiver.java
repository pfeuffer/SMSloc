package de.pfeufferweb.android.whereru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = context.getSharedPreferences(
				Settings.SETTINGS, Context.MODE_PRIVATE);
		String requestText = prefs.getString(Settings.TRIGGER_TEXT, "WhereRU?");
		boolean active = prefs.getBoolean(Settings.ACTIVE, false);

		Log.d("SmsReceiver", "request text: " + requestText + "; active: "
				+ active);

		Bundle bundle = intent.getExtras();
		if (active && bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String origin = msg.getOriginatingAddress();
				String message = msg.getMessageBody().toString();
				if (message.equals(requestText)) {
					Toast.makeText(context,
							"trying to send position to " + origin,
							Toast.LENGTH_LONG).show();
					Intent startService = new Intent(context, SendService.class);
					startService.putExtra("receiver", origin);
					context.startService(startService);
				}
			}
		}
	}
}
