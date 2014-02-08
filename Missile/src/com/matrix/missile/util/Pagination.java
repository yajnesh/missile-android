package com.matrix.missile.util;

import org.json.JSONArray;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;

public class Pagination extends JsonHttpResponseHandler implements
		OnScrollListener {
	private String mUrl;
	private ViewMissileAdapter mAdapter;
	private ListView mListView;
	private int pageNo = 1;
	private int PAGE_COUNT = 20;
	private int THRESHOLD = 5;

//	// CallBack to hide/show progressBar
//	public interface OnScrollCallback {
//		public void showWhatsHot();
//
//		public void hideWhatsHot();
//	}
//
//	OnScrollCallback onScrollListener;

	
	
	public Pagination(ListView listView, ViewMissileAdapter viewMissileAdapter,
			String url) {
		mListView = listView;
		mAdapter = viewMissileAdapter;
		mUrl = url;
		//this.onScrollListener=onScrollCallback;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

//	private int mLastFirstVisibleItem;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
//		int currentFirstVisibleItem = mListView.getFirstVisiblePosition();
//Log.e("ScrollTest", currentFirstVisibleItem+ "" );
//			if (mListView.getLastVisiblePosition() != totalItemCount ) {
//			if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//				onScrollListener.hideWhatsHot();
//			} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//				onScrollListener.showWhatsHot();
//			}
//		}
//		mLastFirstVisibleItem = currentFirstVisibleItem;

		if (pageNo * PAGE_COUNT > totalItemCount)
			return;

		if ((totalItemCount - mListView.getLastVisiblePosition()) <= THRESHOLD) {
			pageNo++;
			getMissileFromServer();
		}
	}

	public void getMissileFromServer() {
		RequestParams requestParams = new RequestParams("page",
				String.valueOf(pageNo));
		MissileRestClient.get(mUrl, requestParams, this);
	}

	@Override
	public void onSuccess(JSONArray missilesJsonArray) {
		Gson gson = new Gson();
		Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
				Missile[].class);
		mAdapter.supportAddAll(missiles);
	}

}
