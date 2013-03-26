package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.widget.Toast;
import de.pfeufferweb.android.whereru.repository.LocationRequest;
import de.pfeufferweb.android.whereru.repository.RequestRepository;
import de.pfeufferweb.android.whereru.repository.SimpleLocation;

public class RequestHandler {
	private final Context context;
	private final Notifications notifications;

	public RequestHandler(Context context) {
		this.context = context;
		this.notifications = new Notifications(context);
	}

	ActiveLocationRequest newRequest(String receiver) {
		LocationRequest request = new RequestRepository(context)
				.createRequest(receiver);
		int notificationId = notifications.newRequest(receiver);
		Toast.makeText(context,
				context.getString(R.string.toastRequest, receiver),
				Toast.LENGTH_LONG).show();
		ListenActivityBroadcast.updateActivity(context);
		return new ActiveLocationRequest(request, notificationId);
	}

	void noGps(ActiveLocationRequest request) {
		request.request.setNoGps();
		updateRequest(request.request);
		notifications.noGps(request.request.getRequester(),
				request.notificationId);
	}

	void success(ActiveLocationRequest request, SimpleLocation location) {
		request.request.setSuccess(location);
		updateRequest(request.request);
		notifications.success(request.request.getRequester(),
				request.notificationId);
	}

	void aborted(ActiveLocationRequest request) {
		request.request.setAborted();
		updateRequest(request.request);
		notifications.aborted(request.request.getRequester(),
				request.notificationId);
	}

	void noFix(ActiveLocationRequest request) {
		request.request.setNoLocation();
		updateRequest(request.request);
		notifications.success(request.request.getRequester(),
				request.notificationId);
	}

	private void updateRequest(LocationRequest request) {
		new RequestRepository(context).updateRequest(request);
		ListenActivityBroadcast.updateActivity(context);
	}
}
