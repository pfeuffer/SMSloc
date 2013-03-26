package de.pfeufferweb.android.whereru;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ListenActivityBroadcast {
	private static final String BROADCAST_NEW_REQUEST = "newRequest";

	static void updateActivity(Context context) {
		Intent intent = new Intent(
				ListenActivityBroadcast.BROADCAST_NEW_REQUEST);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	static void register(Context context, BroadcastReceiver receiver) {
		LocalBroadcastManager.getInstance(context)
				.registerReceiver(
						receiver,
						new IntentFilter(
								ListenActivityBroadcast.BROADCAST_NEW_REQUEST));
	}
}
