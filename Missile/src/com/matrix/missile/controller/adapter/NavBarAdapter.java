package com.matrix.missile.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.model.NavigationDrawerModel;

public class NavBarAdapter extends SupportArrayAdapter<NavigationDrawerModel> {
	private int selectedIndex = 0;

	public NavBarAdapter(Context context) {
		super(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		NavigationDrawerModel navModel = getItem(position);

		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.tvNavBarItem);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.title.setText(navModel.getNavItem());
		int bgColor = android.R.color.transparent;
		if (position == selectedIndex)
			bgColor = R.color.manu_item_selected;
		convertView.setBackgroundResource(bgColor);
		return convertView;
	}

	public void changeSelected(int position) {
		selectedIndex = position;
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView title;
	}
}
