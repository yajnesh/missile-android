package com.matrix.missile.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.ViewMissileAdapter;
import com.matrix.missile.model.Missile;
import com.matrix.missile.util.Pagination;

public class ViewMissilesActivity extends Fragment {

	protected static final String LOG_TAG = "ViewMissilesActivity";
	private ViewMissileAdapter mViewMissileAdapter;
	private ListView listView;
	private Pagination pagination;
	private String mUrl;
	private View rootView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_missile,
				container, false);
		return rootView;
	}

	
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl=getArguments().getString("url");
		initListView();
		pagination.getMissileFromServer();
	}

	private void initListView() {
		listView = (ListView) rootView.findViewById(R.id.lv_missiles);
		mViewMissileAdapter = new ViewMissileAdapter(getActivity());
		listView.setAdapter(mViewMissileAdapter);
		pagination = new Pagination(listView, mViewMissileAdapter, mUrl);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
	}

	OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			
			
			FragmentManager fragmentManager2 = getFragmentManager();
			FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
			MissileActivity fragment2 = new MissileActivity();
			
			Missile missile = (Missile) listView.getItemAtPosition(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable("missile", missile);
			fragment2.setArguments(bundle);

			
			fragmentTransaction2.addToBackStack(null);
			fragmentTransaction2.hide(ViewMissilesActivity.this);
			fragmentTransaction2.add(android.R.id.content, fragment2);
			fragmentTransaction2.commit();
			
			
//			Missile missile = (Missile) listView.getItemAtPosition(position);
//			Intent intent = new Intent(ViewMissilesActivity.this,
//					MissileActivity.class);
//			intent.putExtra("missile", missile);
//			startActivity(intent);
		}
	};
}
