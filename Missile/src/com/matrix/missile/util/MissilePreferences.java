package com.matrix.missile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MissilePreferences {

	public final String PREFS_NAME = "MissilePrefsFile";
	private Context context;
	private SharedPreferences settings;
	private static MissilePreferences instance;
	private final String USER_ID = "userId";

	private MissilePreferences(Context c) {
		context = c;
		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

	}

	public static MissilePreferences getInstance(Context c) {
		if (instance == null) {
			if (c == null) {
				return null;
			}
			instance = new MissilePreferences(c);
		}
		return instance;
	}

	public void setUserId(String userId) {
		settings.edit().putString(USER_ID, userId).commit();
	}

	public String getUserId() {
		return settings.getString(USER_ID, "");
	}
}