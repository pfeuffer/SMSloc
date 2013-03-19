package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RequestSQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_REQUESTS = "requests";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_REQUESTER = "requester";
	public static final String COLUMN_TIME = "time";

	private static final String DATABASE_NAME = "requests.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_REQUESTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_REQUESTER
			+ " text not null, " + COLUMN_TIME + " integer not null);";

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
	}
}
