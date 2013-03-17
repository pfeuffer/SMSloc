package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Settings {
	static final String ACTIVE = "active";
	static final String TRIGGER_TEXT = "trigger";
	static final String SECONDS = "seconds";

	public static void setActive(Context context, boolean active) {
		Editor editor = getEditor(context);
		editor.putBoolean(ACTIVE, active);
		editor.commit();
	}

	public static void setRequestText(Context context, String text) {
		Editor editor = getEditor(context);
		editor.putString(TRIGGER_TEXT, text);
		editor.commit();
	}

	public static void setSeconds(Context context, int seconds) {
		Editor editor = getEditor(context);
		editor.putInt(SECONDS, seconds);
		editor.commit();
	}

	public static boolean getActive(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return prefs.getBoolean(ACTIVE, false);
	}

	public static String getRequestText(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return prefs.getString(TRIGGER_TEXT, "WhereRU?");
	}

	public static int getSeconds(Context context) {
		SharedPreferences prefs = getPrefs(context);
		return prefs.getInt(SECONDS, 3);
	}

	private static Editor getEditor(Context context) {
		SharedPreferences prefs = getPrefs(context);
		Editor editor = prefs.edit();
		return editor;
	}

	private static SharedPreferences getPrefs(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
