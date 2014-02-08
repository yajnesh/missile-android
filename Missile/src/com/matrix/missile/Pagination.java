package com.matrix.missile;

import org.json.JSONArray;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;

public class Pagination  extends JsonHttpResponseHandler implements OnScrollListener{
	private String mUrl;
	private ViewMissileAdapter mAdapter;
	private ListView mListView;
	private int pageNo=1;
	private int PAGE_COUNT = 20;
	private int THRESHOLD = 5;
	public Pagination(ListView listView,ViewMissileAdapter viewMissileAdapter,String url){
		mListView=listView;
		mAdapter=viewMissileAdapter;
		mUrl=url;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

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
		MissileRestClient.get(mUrl, requestParams,
				this);
	}
	
	@Override
	public void onSuccess(JSONArray missilesJsonArray) {
		Gson gson = new Gson();
		Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
				Missile[].class);
		mAdapter.supportAddAll(missiles);
	}

}
