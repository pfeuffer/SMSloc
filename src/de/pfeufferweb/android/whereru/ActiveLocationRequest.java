package de.pfeufferweb.android.whereru;

import de.pfeufferweb.android.whereru.repository.LocationRequest;

class ActiveLocationRequest {
	final LocationRequest request;
	final int notificationId;

	ActiveLocationRequest(LocationRequest request, int notificationId) {
		this.request = request;
		this.notificationId = notificationId;
	}
}
