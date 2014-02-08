package com.matrix.missile.controller.tag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.matrix.missile.view.customviews.component.LinkEnabledTextView.TextLinkClickListener;

public class TagController implements TextLinkClickListener {
	private FragmentManager fragmentManager;
	private Fragment fragment;
	private Fragment fragmentToHide;
	

	public TagController(FragmentManager fragmentManager,Fragment fragment,Fragment fragmentToHide) {
		this.fragmentManager = fragmentManager;
		this.fragment=fragment;
		this.fragmentToHide=fragmentToHide;
	}

	@Override
	public void onTextLinkClick(View textView, String clickedString) {

		FragmentTransaction fragmentTransaction2 = fragmentManager
				.beginTransaction();

		Bundle bundle = new Bundle();
		bundle.putString("url", "tags/" + clickedString.replace("#", "")+ ".json");
		fragment.setArguments(bundle);

		fragmentTransaction2.addToBackStack(null);
		fragmentTransaction2.hide(fragmentToHide);
		fragmentTransaction2.add(android.R.id.content, fragment);
		fragmentTransaction2.commit();

	}
}
