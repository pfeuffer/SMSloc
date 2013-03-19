package de.pfeufferweb.android.whereru;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class Request {

	private final long id;
	private final String requester;
	private final long time;

	public Request(long id, String requester, long time) {
		this.id = id;
		this.requester = requester;
		this.time = time;
	}

	public String toString(Context context) {
		return requester + " ("
				+ DateFormat.getDateFormat(context).format(new Date(time))
				+ ", "
				+ DateFormat.getTimeFormat(context).format(new Date(time))
				+ ")";
	}
}
