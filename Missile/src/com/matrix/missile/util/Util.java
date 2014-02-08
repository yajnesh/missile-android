package com.matrix.missile.util;

import android.content.Context;
import android.widget.Toast;


public class Util {

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

}
