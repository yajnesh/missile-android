package com.matrix.missile.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.Pagination;

public class ViewMissilesActivity extends Activity {

	protected static final String LOG_TAG = "ViewMissilesActivity";
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private Pagination pagination;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile);
		mUrl=getIntent().getStringExtra("url");
		initListView();
		pagination.getMissileFromServer();
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(ViewMissilesActivity.this);
		listView.setAdapter(mViewMissileAdapter);
		pagination = new Pagination(listView, mViewMissileAdapter, mUrl);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
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
}
