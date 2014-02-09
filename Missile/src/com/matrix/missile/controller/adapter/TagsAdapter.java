package com.matrix.missile.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.missile.R;

public class TagsAdapter extends SupportArrayAdapter<String> {
	public TagsAdapter(Context context) {
		super(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		String navModel = getItem(position);

		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.tag_list_item,
					null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTagName);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.title.setText(navModel);
		return convertView;
	}

	private class ViewHolder {
		public TextView title;
	}
}
