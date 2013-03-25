package de.pfeufferweb.android.whereru.repository;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class LocationRequest {

	private final long id;
	private final String requester;
	private final long time;
	private SimpleLocation location;
	private Status status;

	LocationRequest(long id, String requester, long time) {
		this.id = id;
		this.requester = requester;
		this.time = time;
		this.location = null;
		this.status = Status.RUNNING;
	}

	long getId() {
		return id;
	}

	public String getRequester() {
		return requester;
	}

	public long getTime() {
		return time;
	}

	public SimpleLocation getLocation() {
		return location;
	}

	public Status getStatus() {
		return status;
	}

	public void setSuccess(SimpleLocation location) {
		this.status = Status.SUCCESS;
		this.location = location;
	}

	public void setNoLocation() {
		this.status = Status.NO_LOCATION;
		this.location = null;
	}

	public String toString(Context context) {
		return requester + " ("
				+ DateFormat.getDateFormat(context).format(new Date(time))
				+ ", "
				+ DateFormat.getTimeFormat(context).format(new Date(time))
				+ ")";
	}
}
