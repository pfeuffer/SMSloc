package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
	static final String SETTINGS = "Settings";
	static final String ACTIVE = "active";
	static final String TRIGGER_TEXT = "trigger";

	public static void setActive(Context context, boolean active) {
		SharedPreferences prefs = context.getSharedPreferences(
				Settings.SETTINGS, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(Settings.ACTIVE, active);
		editor.commit();
	}

	public static void setRequestText(Context context, String text) {
		SharedPreferences prefs = context.getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(Settings.TRIGGER_TEXT, text);
		editor.commit();
	}

	public static boolean getActive(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Settings.SETTINGS, Context.MODE_PRIVATE);
		return prefs.getBoolean(Settings.ACTIVE, false);
	}

	public static String getRequestText(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Settings.SETTINGS, Context.MODE_PRIVATE);
		return prefs.getString(Settings.TRIGGER_TEXT, "WhereRU?");
	}
}
