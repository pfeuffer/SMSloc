package de.pfeufferweb.android.whereru;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Settings {
	private static final String ACTIVE = "active";
	private static final String TRIGGER = "trigger";
	private static final String TIME = "time";
	private static final String USE_NETWORK = "useNetwork";
	private static final String GOOGLE_MAPS = "googleMapsCompatible";

	private final Context context;

	public Settings(Context context) {
		this.context = context;
	}

	public void setActive(boolean active) {
		SharedPreferences prefs = getPrefs();
		Editor editor = prefs.edit();
		editor.putBoolean(ACTIVE, active);
		editor.commit();
	}

	public boolean getActive() {
		SharedPreferences prefs = getPrefs();
		return prefs.getBoolean(ACTIVE, false);
	}

	public boolean getUseNetwork() {
		SharedPreferences prefs = getPrefs();
		return prefs.getBoolean(USE_NETWORK, false);
	}

	public String getRequestText() {
		SharedPreferences prefs = getPrefs();
		return prefs.getString(TRIGGER,
				context.getString(R.string.defaultTrigger));
	}

	public int getSeconds() {
		SharedPreferences prefs = getPrefs();
		return Integer.parseInt(prefs.getString(TIME,
				context.getString(R.string.defaultTime)));
	}

	public boolean getGoogleMaps() {
		SharedPreferences prefs = getPrefs();
		return prefs.getBoolean(GOOGLE_MAPS, true);
	}

	private SharedPreferences getPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

}
