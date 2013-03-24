package de.pfeufferweb.android.whereru;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

public class RequestRepository {

	private SQLiteDatabase database;
	private final RequestSQLiteHelper dbHelper;
	private final String[] allColumns = { RequestSQLiteHelper.COLUMN_ID,
			RequestSQLiteHelper.COLUMN_REQUESTER,
			RequestSQLiteHelper.COLUMN_TIME,
			RequestSQLiteHelper.COLUMN_LONGITUDE,
			RequestSQLiteHelper.COLUMN_LATITUDE };

	public RequestRepository(Context context) {
		dbHelper = new RequestSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Request createRequest(String requester, Location location) {
		ContentValues values = new ContentValues();
		values.put(RequestSQLiteHelper.COLUMN_REQUESTER, requester);
		values.put(RequestSQLiteHelper.COLUMN_TIME, System.currentTimeMillis());
		values.put(RequestSQLiteHelper.COLUMN_LONGITUDE,
				location == null ? null : location.getLongitude());
		values.put(RequestSQLiteHelper.COLUMN_LATITUDE, location == null ? null
				: location.getLatitude());
		long insertId = database.insert(RequestSQLiteHelper.TABLE_REQUESTS,
				null, values);
		Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUESTS,
				allColumns, RequestSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Request newRequest = cursorToRequest(cursor);
		cursor.close();
		return newRequest;
	}

	public List<Request> getAllRequests() {
		List<Request> requests = new ArrayList<Request>();

		Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUESTS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Request request = cursorToRequest(cursor);
			requests.add(request);
			cursor.moveToNext();
		}
		cursor.close();
		return requests;
	}

	public void deleteAllRequests() {
		database.delete(RequestSQLiteHelper.TABLE_REQUESTS, null, null);
	}

	private Request cursorToRequest(Cursor cursor) {
		SimpleLocation location;
		if (cursor.getFloat(3) == 0)
			location = null;
		else
			location = new SimpleLocation(cursor.getFloat(3),
					cursor.getFloat(4));
		return new Request(cursor.getLong(0), cursor.getString(1),
				cursor.getLong(2), location);
	}
}
