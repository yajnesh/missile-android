package com.matrix.missile.view.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.Pagination;

public class ViewMissilesFragment extends Fragment {

	protected static final String LOG_TAG = "ViewMissilesActivity";
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private TextSwitcher tvHotMissile;
	private Pagination pagination;
	private String mUrl;
	private int mInterval = 10000; // 5 seconds by default, can be changed later
	private Handler handler;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.activity_missile, container, false);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getArguments().getString("url");
		initListView();
		pagination.getMissileFromServer();
		getHotMissileFromServer();
		startRepeatingTask();
	}

	private void initListView() {
		listView = (ListView) rootView.findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(getActivity());
		listView.setAdapter(mViewMissileAdapter);
		pagination = new Pagination(listView, mViewMissileAdapter, mUrl);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
		tvHotMissile = (TextSwitcher) rootView.findViewById(R.id.tvHotMissile);
		// Set the ViewFactory of the TextSwitcher that will create TextView
		// object when asked
		tvHotMissile.setFactory(new ViewFactory() {

			public View makeView() {
				TextView myText = (TextView) getActivity().getLayoutInflater()
						.inflate(R.layout.custom_text_view, null);
				return myText;
			}
		});
		// Declare the in and out animations and initialize them
		Animation in = AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.slide_out_right);

		// set the animation type of textSwitcher
		tvHotMissile.setInAnimation(in);
		tvHotMissile.setOutAnimation(out);
		handler = new Handler();
	}

	OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			FragmentManager fragmentManager2 = getFragmentManager();
			FragmentTransaction fragmentTransaction2 = fragmentManager2
					.beginTransaction();
			MissileFragment fragment2 = new MissileFragment();

			Missile missile = (Missile) listView.getItemAtPosition(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable("missile", missile);
			fragment2.setArguments(bundle);

			fragmentTransaction2.addToBackStack(null);
			fragmentTransaction2.hide(ViewMissilesFragment.this);
			fragmentTransaction2.add(android.R.id.content, fragment2);
			fragmentTransaction2.commit();

			// Missile missile = (Missile) listView.getItemAtPosition(position);
			// Intent intent = new Intent(ViewMissilesActivity.this,
			// MissileActivity.class);
			// intent.putExtra("missile", missile);
			// startActivity(intent);
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
		getHotMissileFromServer();
	}

	private void getHotMissileFromServer() {
		// http://localhost:3000/missiles/hot_missile.json
		MissileRestClient.get("missiles/hot_missile.json", null,
				jsonHotMissileResponseHandler);
	}

	private JsonHttpResponseHandler jsonHotMissileResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject missilesJsonObject) {
			try {
				String message = missilesJsonObject.getString("message");
				tvHotMissile.setText(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private JsonHttpResponseHandler jsonLatestMissileResponseHandler = new JsonHttpResponseHandler() {
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
				jsonLatestMissileResponseHandler);
	}

	private void startRepeatingTask() {
		mStatusChecker.run();
	}

	private void stopRepeatingTask() {
		handler.removeCallbacks(mStatusChecker);
	}
}
