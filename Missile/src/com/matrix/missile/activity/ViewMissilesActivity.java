package com.matrix.missile.activity;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.MissileRestClient;
import com.matrix.missile.R;
import com.matrix.missile.model.Missile;

public class ViewMissilesActivity extends Activity {

	protected static final String LOG_TAG = "ViewMissilesActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile);
		RequestParams requestParams = new RequestParams("page", "1");
		MissileRestClient.get("missiles.json", requestParams,
				jsonHttpGetResponseHandler);
	}

	private JsonHttpResponseHandler jsonHttpGetResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray missilesJsonArray) {
			Gson gson = new Gson();
			Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
					Missile[].class);
			for (Missile missile : missiles) {
				Log.e(LOG_TAG, missile.getTitle());
			}
		}
	};

}
