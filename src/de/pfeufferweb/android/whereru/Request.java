package de.pfeufferweb.android.whereru;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class Request {

	private final String requester;
	private final long time;
	private final SimpleLocation location;

	public Request(long id, String requester, long time, SimpleLocation location) {
		this.requester = requester;
		this.time = time;
		this.location = location;
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

	public String toString(Context context) {
		return requester + " ("
				+ DateFormat.getDateFormat(context).format(new Date(time))
				+ ", "
				+ DateFormat.getTimeFormat(context).format(new Date(time))
				+ ")";
	}
}
