package com.matrix.missile;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MissileRestClient {
	// public static final String BASE_URL =
	// "http://missile.herokuapp.com/missiles/";
	public static final String BASE_URL = "http://192.168.0.106:3000/missiles";
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Accept", "application/json");
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(Context context, String url, StringEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Accept", "application/json");
		client.post(context, getAbsoluteUrl(url), entity, "application/json",
				responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
