package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RequestSQLiteHelper extends SQLiteOpenHelper {
	static final String COLUMN_LATITUDE = "latitude";
	static final String COLUMN_LONGITUDE = "longitude";
	static final String TABLE_REQUESTS = "requests";
	static final String COLUMN_ID = "_id";
	static final String COLUMN_REQUESTER = "requester";
	static final String COLUMN_TIME = "time";

	private static final String DATABASE_NAME = "requests.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_REQUESTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_REQUESTER
			+ " text not null, " + COLUMN_TIME + " integer not null, "
			+ COLUMN_LONGITUDE + " real, " + COLUMN_LATITUDE + " real);";

	public RequestSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.d("RequestSQLiteHelper", "updating from " + oldVersion + " to "
				+ newVersion);
		switch (oldVersion) {
		case 2:
			database.execSQL("alter table " + TABLE_REQUESTS + " add column "
					+ COLUMN_LONGITUDE + " real");
			database.execSQL("alter table " + TABLE_REQUESTS + " add column "
					+ COLUMN_LATITUDE + " real");
			break;
		default:
			break;
		}
	}
}
