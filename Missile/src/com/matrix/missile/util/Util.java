package com.matrix.missile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
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

	public static String getHumanReadableUpdateAt(String dateTimeString) {
		String result;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					MissileConstance.HUMAN_READABLE_DATE_FORMATE,
					Locale.getDefault());
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			result = dateFormat.format(getCalender(dateTimeString).getTime());
		} catch (ParseException e) {
			result = "Unkonwn";
		}
		return result;
	}

	private static Calendar getCalender(String dateTimeString)
			throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				MissileConstance.DEFAULT_DATE_FORMATE, Locale.getDefault());
		calendar.setTime(dateFormat.parse(dateTimeString));
		return calendar;
	}
}
