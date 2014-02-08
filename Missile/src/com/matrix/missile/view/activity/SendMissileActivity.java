package com.matrix.missile.view.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.matrix.missile.R;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.Util;
import com.matrix.missile.util.db.MissileIdDataSource;

public class SendMissileActivity extends Activity implements OnClickListener {
	private final static String Tag = "Missile";
	private EditText editTitle;
	private EditText editMessage;
	private MissileIdDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
		datasource = new MissileIdDataSource(this);
		datasource.open();
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
				Util.showToast(SendMissileActivity.this, "Please enter message");
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
		MissileRestClient.post(SendMissileActivity.this, "missiles.json",
				entity, jsonHttpPostResponseHandler);
	}

	private JsonHttpResponseHandler jsonHttpPostResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int status, JSONObject response) {
			super.onSuccess(response);
			try {
				datasource.insertMissileId(response.getInt("id"));
			} catch (JSONException e) {

				e.printStackTrace();
			}
			Util.showToast(SendMissileActivity.this,
					"Missile launched successfully");
		}

		@Override
		public void onFailure(int status, Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
			if (errorResponse != null) {
				Log.d(Tag, errorResponse.toString());
			}
			Util.showToast(SendMissileActivity.this,
					"Error in Missile launch , status code:" + status);
		}
	};

	public void getMissiles(View v) {
		Intent intent = new Intent(this, ViewMissilesActivity.class);
		// intent.putExtra("url", "missiles.json");
		intent.putExtra("url", "tags/yajnesh.json");
		startActivity(intent);

	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
