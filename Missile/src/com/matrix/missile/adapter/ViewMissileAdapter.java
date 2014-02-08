package com.matrix.missile.adapter;

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
			convertView = getLayoutInflater().inflate(R.layout.item, parent,false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.desc = (TextView) convertView.findViewById(R.id.tvMessage);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		
		holder.title.setText(getItem(position).getTitle());
		holder.desc.setText(getItem(position).getMessage());
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView desc;
	}

}
