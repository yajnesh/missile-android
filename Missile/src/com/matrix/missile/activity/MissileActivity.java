package com.matrix.missile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.R.id;
import com.matrix.missile.R.layout;
import com.matrix.missile.model.Missile;

public class MissileActivity extends Activity {
	private TextView tvTitle;
	private TextView tvMessage;
	private Missile missile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missile_layout);
		missile=getIntent().getParcelableExtra("missile");
		initialize();
		tvTitle.setText(missile.getTitle());
		tvMessage.setText(missile.getMessage());
	}

	private void initialize() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvMessage = (TextView) findViewById(R.id.tvMessage);
	}
}
