package com.matrix.missile.controller.tag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.matrix.missile.controller.adapter.StartModule;
import com.matrix.missile.view.customviews.component.LinkEnabledTextView.TextLinkClickListener;

public class TagController implements TextLinkClickListener {
	private FragmentManager fragmentManager;
	private Fragment fragment;
	private Fragment fragmentToHide;

	public TagController(FragmentManager fragmentManager, Fragment fragment,
			Fragment fragmentToHide) {
		this.fragmentManager = fragmentManager;
		this.fragment = fragment;
		this.fragmentToHide = fragmentToHide;
	}

	@Override
	public void onTextLinkClick(View textView, String clickedString) {

		Bundle bundle = new Bundle();
		bundle.putString("url", "tags/" + clickedString.replace("#", "")
				+ ".json");
		fragment.setArguments(bundle);
		StartModule.addFragmentForModule(fragmentManager, fragment);
	}
}
