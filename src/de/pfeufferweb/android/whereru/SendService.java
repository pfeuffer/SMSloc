package de.pfeufferweb.android.whereru;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendService extends Service {
	private LocationManager locationManager;
	private final IBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {
		SendService getService() {
			return SendService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("SendService", "startet");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Log.d("SendService",
				"GPS enabled: "
						+ locationManager
								.isProviderEnabled(LocationManager.GPS_PROVIDER));
		String receiver = intent.getExtras().getString("receiver");
		int notificationId = intent.getExtras().getInt("notificationId");
		getPosition(receiver, notificationId);
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private void getPosition(final String receiver, final int notificationId) {
		Location location = new TimedLocationProvider(this, 20000, 20)
				.getBestLocation();
		String text = format(location);
		Log.d("SendService", "text: " + text);
		Toast.makeText(SendService.this, text, Toast.LENGTH_LONG).show();
		sendSMS(text, receiver, notificationId);
	}

	private void sendSMS(String message, final String receiver,
			final int notificationId) {
		PendingIntent pi = PendingIntent.getActivity(SendService.this, 0,
				new Intent(SendService.this, SmsReceiver.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(receiver, null, message, pi, null);
		updateNotification(SendService.this, receiver, message, notificationId);
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
				.setContentInfo("position sent to " + phoneNumber)
				.setContentText(message)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								Intent.ACTION_VIEW), 0)).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}
}
