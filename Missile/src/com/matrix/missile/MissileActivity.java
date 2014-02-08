package com.matrix.missile;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

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
		android.util.Log.e("LOG", "yperlink clicked is :: " + clickedString
				+ "  Hyperlink clicked is :: " + clickedString);
	}
}