package com.matrix.missile.activity;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.MissileActivity;
import com.matrix.missile.MissileRestClient;
import com.matrix.missile.R;
import com.matrix.missile.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;

public class ViewMissilesActivity extends Activity implements OnScrollListener {

	protected static final String LOG_TAG = "ViewMissilesActivity";
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private int pageNo = 1;
	private int PAGE_COUNT = 20;
	private int THRESHOLD = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile);
		initListView();
		getMissileFromServer(pageNo);
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(ViewMissilesActivity.this);
		listView.setAdapter(mViewMissileAdapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(listener);
	}

	OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Missile missile = (Missile) listView.getItemAtPosition(position);
			Intent intent=new Intent(ViewMissilesActivity.this,MissileActivity.class);
			intent.putExtra("missile",missile);
			startActivity(intent);
		}
	};

	private void getMissileFromServer(int num) {
		RequestParams requestParams = new RequestParams("page",
				String.valueOf(num));
		MissileRestClient.get("missiles.json", requestParams,
				jsonHttpGetResponseHandler);
	}

	private JsonHttpResponseHandler jsonHttpGetResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray missilesJsonArray) {
			Gson gson = new Gson();
			Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
					Missile[].class);
			mViewMissileAdapter.supportAddAll(missiles);
		}
	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (pageNo * PAGE_COUNT > totalItemCount)
			return;

		if ((totalItemCount - listView.getLastVisiblePosition()) <= THRESHOLD) {
			pageNo++;
			getMissileFromServer(pageNo);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
