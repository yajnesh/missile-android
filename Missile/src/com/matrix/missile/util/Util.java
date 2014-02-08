package com.matrix.missile.util;

import java.util.UUID;

import android.content.Context;
import android.widget.Toast;

public class Util {

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	public static void getId(Context context) {
		// get
		String old = MissilePreferences.getInstance(context).getUserId();
		// store
		MissilePreferences.getInstance(context).setUserId(
				UUID.randomUUID().toString());
	}

}
