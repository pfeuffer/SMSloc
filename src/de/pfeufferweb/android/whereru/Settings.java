package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Settings {
	private static final String ACTIVE = "active";
	private static final String TRIGGER = "trigger";
	private static final String TIME = "time";

	public static void setActive(Context context, boolean active) {
		SharedPreferences prefs = getPrefs(context);
		Editor editor = prefs.edit();
		editor.putBoolean(ACTIVE, active);
		editor.commit();
	}

	public static boolean getActive(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return prefs.getBoolean(ACTIVE, false);
	}

	public static String getRequestText(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return prefs.getString(TRIGGER, "WhereRU?");
	}

	public static int getSeconds(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return Integer.parseInt(prefs.getString(TIME, "3"));
	}

	private static SharedPreferences getPrefs(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
