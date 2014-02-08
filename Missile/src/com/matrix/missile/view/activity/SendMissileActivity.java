package com.matrix.missile.view.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.Util;
import com.matrix.missile.util.db.MissileIdDataSource;

public class SendMissileActivity extends Fragment implements OnClickListener {
	private final static String Tag = "Missile";
	private EditText editTitle;
	private EditText editMessage;
	private MissileIdDataSource datasource;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_main,
				container, false);
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
		datasource = new MissileIdDataSource(getActivity());
		datasource.open();
	}

	private void initialize() {

		editTitle = (EditText) rootView.findViewById(R.id.editTitle);
		editMessage = (EditText) rootView.findViewById(R.id.editMessage);
		((Button) rootView.findViewById(R.id.btnSend)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSend:
			if (editMessage.getText().toString().trim().equals("")) {
				Util.showToast(getActivity(), "Please enter message");
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
		MissileRestClient.post(getActivity(), "missiles.json",
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
			Util.showToast(getActivity(),
					"Missile launched successfully");
		}

		@Override
		public void onFailure(int status, Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
			if (errorResponse != null) {
				Log.d(Tag, errorResponse.toString());
			}
			Util.showToast(getActivity(),
					"Error in Missile launch , status code:" + status);
		}
	};

	
	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}


}
