package com.matrix.missile.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.matrix.missile.R;

public class StartModule {

	public static void addFragmentForModule(FragmentManager ftt,
			final Fragment fragment) {
		//ftt.executePendingTransactions();
		FragmentTransaction ft = ftt.beginTransaction();
		ft.replace(R.id.frame_container, fragment, fragment.getClass()
				.getSimpleName());
		ft.addToBackStack(fragment.getClass().getSimpleName());
		ft.commitAllowingStateLoss();
		ftt.executePendingTransactions();
	}

	public static void setFragmentLayout(FragmentManager ftt,
			final Fragment fragment, Bundle bundle, String fragId) {
		fragment.setArguments(bundle);
		ftt.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		ftt.executePendingTransactions();
		FragmentTransaction ft = ftt.beginTransaction();
		ft.replace(R.id.frame_container, fragment, fragId);
		ft.addToBackStack(fragId);
		ft.commitAllowingStateLoss();
		ftt.executePendingTransactions();
	}
}
