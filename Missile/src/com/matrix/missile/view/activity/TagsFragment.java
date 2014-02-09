package com.matrix.missile.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.StartModule;
import com.matrix.missile.controller.adapter.TagsAdapter;
import com.matrix.missile.util.PaginateTags;

public class TagsFragment extends Fragment {
	private ListView listView;
	private View rootView;
	private TagsAdapter tagsAdapter;
	private PaginateTags pagination;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (rootView != null && rootView.getParent() != null) {
			FrameLayout vv = (FrameLayout) rootView.getParent();
			vv.removeView(rootView);
			return rootView;
		}
		rootView = inflater.inflate(R.layout.tags_layout, container, false);
		initListView();
		pagination.getTagFromServer();
		return rootView;
	}

	private void initListView() {
		listView = (ListView) rootView.findViewById(R.id.list);
		tagsAdapter = new TagsAdapter(getActivity());
		listView.setAdapter(tagsAdapter);
		listView.setOnScrollListener(pagination);
		listView.setOnItemClickListener(listener);
		pagination = new PaginateTags(listView, tagsAdapter, "tags.json");
	}

	private OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String tag = (String) listView.getItemAtPosition(position);
			ViewMissilesFragment viewMissilesFragment = new ViewMissilesFragment();
			Bundle bundle = new Bundle();
			bundle.putString("url", "tags/" + tag + ".json");
			bundle.putBoolean("viewall", false);
			bundle.putBoolean("search", false);
			viewMissilesFragment.setArguments(bundle);
			StartModule.addFragmentForModule(getFragmentManager(),
					viewMissilesFragment);
		}
	};
}
