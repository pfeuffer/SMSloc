package de.pfeufferweb.android.whereru;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

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
				if (message
						.trim()
						.toUpperCase(Locale.getDefault())
						.equals(requestText.trim().toUpperCase(
								Locale.getDefault()))) {
					Intent startService = new Intent(context, SendService.class);
					startService.putExtra("receiver", origin);
					context.startService(startService);
				}
			}
		}
	}

}
