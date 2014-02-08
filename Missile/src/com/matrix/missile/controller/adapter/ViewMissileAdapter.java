package com.matrix.missile.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.model.Missile;

public class ViewMissileAdapter extends SupportArrayAdapter<Missile> {

	public ViewMissileAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.main_missile_list_item, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.message = (TextView) convertView
					.findViewById(R.id.tvMessage);
			holder.time = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.title.setText(getItem(position).getTitle());
		holder.message.setText(getItem(position).getMessage());
		holder.time.setText(getItem(position).getTime());
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView message;
		TextView time;
	}

}
