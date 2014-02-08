package com.matrix.missile.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.controller.tag.TagController;
import com.matrix.missile.model.Missile;
import com.matrix.missile.view.customviews.component.LinkEnabledTextView;

public class MissileActivity extends Activity {
	private TextView tvTitle;
	private LinkEnabledTextView tvMessage;
	private Missile missile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile_layout);
		missile = getIntent().getParcelableExtra("missile");
		initialize();
		tvTitle.setText(missile.getTitle());
		tvMessage.setText(missile.getMessage());
		tvMessage.setOnTextLinkClickListener(new TagController(this));
	}

	private void initialize() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvMessage = (LinkEnabledTextView) findViewById(R.id.tvMessage);
	}
}
