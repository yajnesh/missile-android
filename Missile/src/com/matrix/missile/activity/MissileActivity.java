package com.matrix.missile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.component.LinkEnabledTextView;
import com.matrix.missile.component.TextLinkClickListener;
import com.matrix.missile.model.Missile;

public class MissileActivity extends Activity implements TextLinkClickListener {
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
		tvMessage.setLinkEnabledText(missile.getMessage());
		tvMessage.setOnTextLinkClickListener(this);

	}

	private void initialize() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvMessage = (LinkEnabledTextView) findViewById(R.id.tvMessage);

	}

	public void onTextLinkClick(View textView, String clickedString) {
		Intent intent = new Intent(this, ViewMissilesActivity.class);
		intent.putExtra("url", "tags/"+clickedString.replace("#", "")+".json");
		startActivity(intent);
	}
}
