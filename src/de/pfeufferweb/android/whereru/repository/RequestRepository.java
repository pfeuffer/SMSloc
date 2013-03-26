package de.pfeufferweb.android.whereru.repository;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RequestRepository {

	private SQLiteDatabase database;
	private final RequestSQLiteHelper dbHelper;
	private final String[] allColumns = { RequestSQLiteHelper.COLUMN_ID,
			RequestSQLiteHelper.COLUMN_REQUESTER,
			RequestSQLiteHelper.COLUMN_TIME,
			RequestSQLiteHelper.COLUMN_LONGITUDE,
			RequestSQLiteHelper.COLUMN_LATITUDE,
			RequestSQLiteHelper.COLUMN_STATUS };

	public RequestRepository(Context context) {
		dbHelper = new RequestSQLiteHelper(context);
	}

	public LocationRequest createRequest(String requester) {
		open();
		try {
			ContentValues values = new ContentValues();
			values.put(RequestSQLiteHelper.COLUMN_REQUESTER, requester);
			values.put(RequestSQLiteHelper.COLUMN_TIME,
					System.currentTimeMillis());
			values.put(RequestSQLiteHelper.COLUMN_LONGITUDE, (String) null);
			values.put(RequestSQLiteHelper.COLUMN_LATITUDE, (String) null);
			values.put(RequestSQLiteHelper.COLUMN_STATUS,
					Status.RUNNING.getId());
			long insertId = database.insert(RequestSQLiteHelper.TABLE_REQUESTS,
					null, values);
			Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUESTS,
					allColumns, RequestSQLiteHelper.COLUMN_ID + " = "
							+ insertId, null, null, null, null);
			cursor.moveToFirst();
			LocationRequest newRequest = cursorToRequest(cursor);
			cursor.close();
			return newRequest;
		} finally {
			close();
		}
	}

	public void updateRequest(LocationRequest request) {
		open();
		ContentValues values = new ContentValues();
		values.put(RequestSQLiteHelper.COLUMN_REQUESTER, request.getRequester());
		values.put(RequestSQLiteHelper.COLUMN_TIME, request.getTime());
		values.put(RequestSQLiteHelper.COLUMN_LONGITUDE,
				request.getStatus() == Status.SUCCESS ? request.getLocation()
						.getLongitude() : null);
		values.put(RequestSQLiteHelper.COLUMN_LATITUDE,
				request.getStatus() == Status.SUCCESS ? request.getLocation()
						.getLatitude() : null);
		values.put(RequestSQLiteHelper.COLUMN_STATUS, request.getStatus()
				.getId());
		database.update(RequestSQLiteHelper.TABLE_REQUESTS, values,
				RequestSQLiteHelper.COLUMN_ID + " = ?",
				new String[] { Long.toString(request.getId()) });
		close();
	}

	public List<LocationRequest> getAllRequests() {
		open();
		try {
			List<LocationRequest> requests = new ArrayList<LocationRequest>();

			Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUESTS,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				LocationRequest request = cursorToRequest(cursor);
				requests.add(request);
				cursor.moveToNext();
			}
			cursor.close();
			return requests;
		} finally {
			close();
		}
	}

	public void deleteAllRequests() {
		open();
		database.delete(RequestSQLiteHelper.TABLE_REQUESTS, null, null);
		close();
	}

	void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	void close() {
		dbHelper.close();
	}

	private LocationRequest cursorToRequest(Cursor cursor) {
		SimpleLocation location;
		LocationRequest request = new LocationRequest(cursor.getLong(0),
				cursor.getString(1), cursor.getLong(2));
		Status status = Status.getForId(cursor.getInt(5));
		if (status == Status.NO_LOCATION) {
			request.setNoLocation();
		} else if (status == Status.SUCCESS) {
			request.setSuccess(new SimpleLocation(cursor.getFloat(3), cursor
					.getFloat(4)));
		}
		return request;
	}
}
