package com.matrix.missile.view.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.StartModule;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.MissileRestClient;
import com.matrix.missile.util.NetworkListener;
import com.matrix.missile.util.db.MissileIdDataSource;

public class MyMissilesFragment extends Fragment implements NetworkListener {
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private TextView txtEmpty;
	private MissileIdDataSource datasource;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (rootView != null && rootView.getParent() != null) {
			FrameLayout vv = (FrameLayout) rootView.getParent();
			vv.removeView(rootView);
			return rootView;
		}
		rootView = inflater
				.inflate(R.layout.activity_missile, container, false);
		initListView();
		datasource = new MissileIdDataSource(getActivity());
		datasource.open();
		getMyMissilesFromServer();
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initListView() {
		((LinearLayout) rootView.findViewById(R.id.header))
				.setVisibility(View.GONE);
		txtEmpty = (TextView) rootView.findViewById(R.id.tvEmpty);
		listView = (ListView) rootView.findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(getActivity());
		listView.setAdapter(mViewMissileAdapter);
		listView.setOnItemClickListener(listener);
	}

	private OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
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
				if (missiles.length == 0)
					txtEmpty.setText("No results found");
				else
					txtEmpty.setVisibility(View.GONE);

			} catch (JSONException e) {
				txtEmpty.setText("No results found");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onFailure(String responseBody, Throwable error) {
			txtEmpty.setText("No results found");
			Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
		};
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

	@Override
	public void requestStatus(Boolean status) {
		if (status) {
			txtEmpty.setVisibility(View.GONE);
		} else {
			txtEmpty.setText("No results found");
		}
	}
}
