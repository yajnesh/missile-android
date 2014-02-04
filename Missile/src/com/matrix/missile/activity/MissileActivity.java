package com.matrix.missile.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.matrix.missile.MissileRestClient;
import com.matrix.missile.R;
import com.matrix.missile.model.Missile;

public class MissileActivity extends Activity implements OnClickListener {
	private final static String Tag = "Missile";
	private EditText editTitle;
	private EditText editMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
	}

	private void initialize() {
		editTitle = (EditText) findViewById(R.id.editTitle);
		editMessage = (EditText) findViewById(R.id.editMessage);
		((Button) findViewById(R.id.btnSend)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSend:
			if (editMessage.getText().toString().trim().equals("")) {
				showToast("Please enter message");
				editMessage.requestFocus();
			} else {
				createMissile();
			}
			break;
		}
	}

	private void createMissile() {
		JSONObject jsonParams = new JSONObject();
		StringEntity entity = null;
		try {
			jsonParams.put("title", editTitle.getText().toString());
			jsonParams.put("message", editMessage.getText().toString());
			entity = new StringEntity(jsonParams.toString());
		} catch (JSONException e1) {
			Log.e(Tag, e1.getMessage());
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			Log.e(Tag, e1.getMessage());
			e1.printStackTrace();
		}
		MissileRestClient.post(MissileActivity.this, "", entity,
				jsonHttpPostResponseHandler);
	}

	private JsonHttpResponseHandler jsonHttpPostResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int status, JSONObject response) {
			super.onSuccess(response);
			showToast("Missile launched successfully");
		}

		@Override
		public void onFailure(int status, Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
			if (errorResponse != null) {
				Log.d(Tag, errorResponse.toString());
			}
			showToast("Error in Missile launch , status code:" + status);
		}
	};

	private void showToast(String str) {
		Toast.makeText(MissileActivity.this, str, Toast.LENGTH_LONG).show();
	}

	public void getMissiles(View v) {
		MissileRestClient.get("", null, jsonHttpGetResponseHandler);
	}

	private JsonHttpResponseHandler jsonHttpGetResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray missiles) {
			ArrayList<Missile> missileArrayList = new ArrayList<Missile>();
			try {
				for (int i = 0; i < missiles.length(); i++) {
					JSONObject jsonObject = (JSONObject) missiles.get(i);
					Missile missile = new Missile();
					missile.setMessage(jsonObject.getString("message"));
					missile.setTitle(jsonObject.getString("title"));
					missileArrayList.add(missile);
				}

			} catch (JSONException e) {
				Log.e(Tag, e.getMessage());
				e.printStackTrace();
			}
			showToast("result" + missileArrayList.toString());
		}
	};
}
