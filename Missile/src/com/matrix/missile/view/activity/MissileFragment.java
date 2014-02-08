package com.matrix.missile.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.controller.tag.TagController;
import com.matrix.missile.model.Missile;
import com.matrix.missile.view.customviews.component.LinkEnabledTextView;

public class MissileFragment extends Fragment {
	private TextView tvTitle;
	private LinkEnabledTextView tvMessage;
	private Missile missile;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_missile_layout,
				container, false);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		missile = getArguments().getParcelable("missile");
		initialize();
		tvTitle.setText(missile.getTitle());
		tvMessage.setText(missile.getMessage());

		ViewMissilesFragment viewMissilesActivity = new ViewMissilesFragment();
		tvMessage.setOnTextLinkClickListener(new TagController(
				getFragmentManager(), viewMissilesActivity, this));
	}

	private void initialize() {
		tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
		tvMessage = (LinkEnabledTextView) rootView.findViewById(R.id.tvMessage);
	}
}
