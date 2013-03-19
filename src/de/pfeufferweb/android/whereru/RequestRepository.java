package de.pfeufferweb.android.whereru;

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
			RequestSQLiteHelper.COLUMN_TIME };

	public RequestRepository(Context context) {
		dbHelper = new RequestSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Request createRequest(String requester) {
		ContentValues values = new ContentValues();
		values.put(RequestSQLiteHelper.COLUMN_REQUESTER, requester);
		values.put(RequestSQLiteHelper.COLUMN_TIME, System.currentTimeMillis());
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

	private Request cursorToRequest(Cursor cursor) {
		return new Request(cursor.getLong(0), cursor.getString(1),
				cursor.getLong(2));
	}
}
