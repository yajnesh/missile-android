package com.matrix.missile.view.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.db.MissileIdDataSource;

public class MyMissilesActivity extends Activity {
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private MissileIdDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile);
		initListView();
		datasource = new MissileIdDataSource(this);
		datasource.open();
		getMyMissilesFromServer();
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(MyMissilesActivity.this);
		listView.setAdapter(mViewMissileAdapter);
		listView.setOnItemClickListener(listener);
	}

	private OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Missile missile = (Missile) listView.getItemAtPosition(position);
			Intent intent = new Intent(MyMissilesActivity.this,
					MissileActivity.class);
			intent.putExtra("missile", missile);
			startActivity(intent);
		}
	};

	private void getMyMissilesFromServer() {
		// http://localhost:3000/missiles/by_ids.json?ids=1,2,300,3,444
		List<Integer> list = datasource.getAllMissileIds();
		RequestParams requestParams = new RequestParams("ids", list.toString());
		MissileRestClient.get("missiles/by_ids.json", requestParams,
				jsonResponseHandler);
	}

	private JsonHttpResponseHandler jsonResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject missilesJsonObject) {
			JSONArray missilesJsonArray = null;
			try {
				missilesJsonArray = missilesJsonObject.getJSONArray("missiles");
				Gson gson = new Gson();
				Missile[] missiles = gson.fromJson(
						missilesJsonArray.toString(), Missile[].class);
				mViewMissileAdapter.supportAddAll(missiles);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onFailure(String responseBody, Throwable error) {
			Toast.makeText(MyMissilesActivity.this, "Failure",
					Toast.LENGTH_LONG).show();
		};
	};

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
