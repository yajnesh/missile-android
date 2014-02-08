package com.matrix.missile.view.activity;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.Pagination;

public class ViewMissilesActivity extends Activity {

	protected static final String LOG_TAG = "ViewMissilesActivity";
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private Pagination pagination;
	private String mUrl;
	private int mInterval = 10000; // 5 seconds by default, can be changed later
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile);
		mUrl = getIntent().getStringExtra("url");
		initListView();
		pagination.getMissileFromServer();
		startRepeatingTask();
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(ViewMissilesActivity.this);
		listView.setAdapter(mViewMissileAdapter);
		pagination = new Pagination(listView, mViewMissileAdapter, mUrl);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
		handler = new Handler();
	}

	OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Missile missile = (Missile) listView.getItemAtPosition(position);
			Intent intent = new Intent(ViewMissilesActivity.this,
					MissileActivity.class);
			intent.putExtra("missile", missile);
			startActivity(intent);
		}
	};

	private Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			if (mViewMissileAdapter.getCount() > 0)
				updateMissileList();
			handler.postDelayed(mStatusChecker, mInterval);
		}

	};

	private void updateMissileList() {
		getLatestMissileFromServer();
	}

	JsonHttpResponseHandler jsonResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray missilesJsonArray) {
			Gson gson = new Gson();
			Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
					Missile[].class);
			mViewMissileAdapter.setNotifyOnChange(false);
			for (int i = missiles.length - 1; i >= 0; i--) {
				mViewMissileAdapter.insert(missiles[i], 0);
			}
			mViewMissileAdapter.notifyDataSetChanged();
		}
	};

	private void getLatestMissileFromServer() {
		// http://localhost:3000/missiles/new_missiles.json?id=12&page=2
		// here page is an optional param
		RequestParams requestParams = new RequestParams("id",
				mViewMissileAdapter.getItem(0).getId());
		MissileRestClient.get("missiles/new_missiles.json", requestParams,
				jsonResponseHandler);
	}

	private void startRepeatingTask() {
		mStatusChecker.run();
	}

	private void stopRepeatingTask() {
		handler.removeCallbacks(mStatusChecker);
	}
}
