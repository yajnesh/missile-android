package com.matrix.missile.controller.tag;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.matrix.missile.view.activity.ViewMissilesActivity;
import com.matrix.missile.view.customviews.component.LinkEnabledTextView.TextLinkClickListener;

public class TagController implements TextLinkClickListener {
	private Activity activity;

	public TagController(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onTextLinkClick(View textView, String clickedString) {
		Intent intent = new Intent(activity, ViewMissilesActivity.class);
		intent.putExtra("url", "tags/" + clickedString.replace("#", "")
				+ ".json");
		activity.startActivity(intent);
	}
}
