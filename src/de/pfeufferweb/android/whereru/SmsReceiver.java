package de.pfeufferweb.android.whereru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String origin = msg.getOriginatingAddress();
				String message = msg.getMessageBody().toString();
				if (message.equals("WhereRU?")) {
					Toast.makeText(context, "Sending position to " + origin,
							Toast.LENGTH_LONG).show();
					Intent startService = new Intent(context, SendService.class);
					startService.putExtra("receiver", origin);
					context.startService(startService);
				}
			}
		}
	}
}