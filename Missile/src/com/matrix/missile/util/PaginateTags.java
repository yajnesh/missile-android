package com.matrix.missile.util;

import org.apache.http.Header;
import org.json.JSONArray;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.controller.adapter.TagsAdapter;

public class PaginateTags extends JsonHttpResponseHandler implements
		OnScrollListener {
	private String mUrl;
	private TagsAdapter mAdapter;
	private ListView mListView;
	private int pageNo = 1;
	private int PAGE_COUNT = 20;
	private int THRESHOLD = 5;
	private NetworkListener networkListener;

	public PaginateTags(ListView listView, TagsAdapter tagsAdapter, String url,
			NetworkListener listener) {
		mListView = listView;
		mAdapter = tagsAdapter;
		mUrl = url;
		networkListener = listener;
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
			getTagFromServer();
		}
	}

	public void getTagFromServer() {
		RequestParams requestParams = new RequestParams("page",
				String.valueOf(pageNo));
		MissileRestClient.get(mUrl, requestParams, this);
	}

	@Override
	public void onSuccess(JSONArray missilesJsonArray) {
		String tags = missilesJsonArray.toString();
		tags = tags.replace("[", "");
		tags = tags.replace("]", "");
		tags = tags.replace("\"", "");

		String str[] = tags.split(",");
		mAdapter.supportAddAll(str);
		networkListener.requestStatus(true);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseBody, Throwable e) {
		networkListener.requestStatus(false);
		super.onFailure(statusCode, headers, responseBody, e);
	}

}
