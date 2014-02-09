package com.matrix.missile.view.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.StartModule;
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
	private HomeScreenActivity activity;
	private LinearLayout mHeaderView;

	private boolean isSearchBarExpanded = false;
	private MenuItem searchItem;
	private Handler delaySearchHandler;
	private Boolean isViewAllMissiles = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		activity = (HomeScreenActivity) getActivity();
		if (rootView != null && rootView.getParent() != null) {
			FrameLayout vv = (FrameLayout) rootView.getParent();
			vv.removeView(rootView);

			return rootView;
		}
		rootView = inflater
				.inflate(R.layout.activity_missile, container, false);
		mUrl = getArguments().getString("url");
		isSearchEnabled = getArguments().getBoolean("search");
		isViewAllMissiles = getArguments().getBoolean("viewall");
		initListView();

		if (!isSearchBarExpanded) {
			pagination.getMissileFromServer();
			getHotMissileFromServer();
			startRepeatingTask();
		}

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void initListView() {
		listView = (ListView) rootView.findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(getActivity());
		listView.setAdapter(mViewMissileAdapter);
		pagination = new Pagination(listView, mViewMissileAdapter, mUrl);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
		tvHotMissile = (TextSwitcher) rootView.findViewById(R.id.tvHotMissile);

		mHeaderView = (LinearLayout) rootView.findViewById(R.id.header);
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
		delaySearchHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 5) {
					search((String) msg.obj);

				}
			}
		};
		// called for listing special missiles like tags, disable header
		if (!isViewAllMissiles)
			mHeaderView.setVisibility(View.GONE);
		else {
			if (isSearchEnabled) {
				mHeaderView.setVisibility(View.GONE);
			} else {
				mHeaderView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Toast.makeText(getActivity(), "aa",
						// Toast.LENGTH_LONG).show();
						MissileFragment missileFragment = new MissileFragment();
						Bundle bundle = new Bundle();
						bundle.putParcelable("missile",
								(Missile) tvHotMissile.getTag());
						missileFragment.setArguments(bundle);
						StartModule.addFragmentForModule(getFragmentManager(),
								missileFragment);
					}
				});
			}
		}
	}

	OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			MissileFragment missileFragment = new MissileFragment();
			Missile missile = (Missile) listView.getItemAtPosition(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable("missile", missile);
			missileFragment.setArguments(bundle);
			StartModule.addFragmentForModule(getFragmentManager(),
					missileFragment);
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
		// getLatestMissileFromServer();
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

				// Missile missile= new Missile();
				// missile.setTitle(missilesJsonObject.getString("title"));
				// missile.setMessage(message);
				//
				Gson gson = new Gson();
				Missile missile = gson.fromJson(missilesJsonObject.toString(),
						Missile.class);
				tvHotMissile.setTag(missile);

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
			// listView.setSelectionFromTop(listView.getFirstVisiblePosition()
			// + missiles.length, 10);
		}
	};
	private boolean isUp = false;

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

	private Boolean isSearchEnabled = false;

	@Override
	public void onPause() {
		stopRepeatingTask();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search_enabled:
			if (!isSearchEnabled) {
				ViewMissilesFragment viewMissilesFragment = new ViewMissilesFragment();
				Bundle bundle = new Bundle();
				bundle.putString("url", "missiles/search/.json");
				bundle.putBoolean("search", true);
				bundle.putBoolean("viewall", false);
				viewMissilesFragment.setArguments(bundle);
				StartModule.addFragmentForModule(getFragmentManager(),
						viewMissilesFragment);
			}
			// http://localhost:3000/missiles/search/yaj.json
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (isViewAllMissiles || isSearchEnabled) {
			inflater.inflate(R.menu.home_screen_activity, menu);
			// Log.d("missile", "craete options");
			setSearchBar(menu);
			// setSortSpinner(menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
	}

	private void setSearchBar(Menu menu) {

		// menu.findItem(R.id.action_bar);

		searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();

		searchItem.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				isSearchBarExpanded = true;
				activity.getDrawerLayout().setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				isSearchBarExpanded = false;
				// Enable drawer opening by left-to-right swipe while
				// search bar is collapsed
				activity.getDrawerLayout().setDrawerLockMode(
						DrawerLayout.LOCK_MODE_UNLOCKED);
				if (isSearchEnabled) {
					if (isResumed()) {
						getFragmentManager().popBackStack();
						getFragmentManager().executePendingTransactions();
					}
				}
				return true;

			}
		});

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String key) {

				if (key != null && key.length() > 0) {
					search(key);
					hideVirtualKeyBoard();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String key) {

				// if (key == null || key.length() == 0) {
				// if (!activity.isDrawerOpen && isSearchBarExpanded) {
				// // selectItem(lastSelectedItemPosition);
				// }
				// } else
				//
				if (isSearchEnabled) {

					delaySearchHandler.removeMessages(5);
					final Message msg = Message.obtain(delaySearchHandler, 5,
							key);
					delaySearchHandler.sendMessageDelayed(msg, 750);

				}

				return true;
			}
		});

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (isViewAllMissiles || isSearchEnabled) {
			super.onPrepareOptionsMenu(menu);
			menu.findItem(R.id.action_search).setVisible(isSearchEnabled);
			menu.findItem(R.id.search_enabled).setVisible(!isSearchEnabled);
			if (isSearchEnabled)
				menu.findItem(R.id.action_search).expandActionView();
		}
	}

	private void search(String key) {
		RequestParams requestParams = new RequestParams("page", 1);
		MissileRestClient.get("missiles/search/" + key + ".json",
				requestParams, jsonSearchMissileResponseHandler);

		// Toast.makeText(getActivity(), "test:" + key,
		// Toast.LENGTH_SHORT).show();

	}

	private JsonHttpResponseHandler jsonSearchMissileResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray missilesJsonArray) {
			Gson gson = new Gson();
			Missile[] missiles = gson.fromJson(missilesJsonArray.toString(),
					Missile[].class);
			mViewMissileAdapter.clear();
			mViewMissileAdapter.supportAddAll(missiles);
		}

		public void onFailure(int statusCode, Throwable e,
				JSONObject errorResponse) {
			mViewMissileAdapter.clear();
		};
	};

	/**
	 * hides the soft keyboard
	 */
	private void hideVirtualKeyBoard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
