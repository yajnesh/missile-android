package com.matrix.missile.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.model.Comment;

public class CommentAdapter extends SupportArrayAdapter<Comment> {

	private static final int NORMAL = 0, SECTION = 1;

	public CommentAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		switch (getItemViewType(position)) {
		case 0:
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.comment_list_item_left_aligned, null);
				viewHolder = new ViewHolder();
				viewHolder.bodyTextView = (TextView) convertView
						.findViewById(R.id.bodyTextView);
				viewHolder.timeTextView = (TextView) convertView
						.findViewById(R.id.timeTextView);
				convertView.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) convertView.getTag();
			break;
		case 1:
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.comment_list_item_right_aligned, null);
				viewHolder = new ViewHolder();
				viewHolder.bodyTextView = (TextView) convertView
						.findViewById(R.id.bodyTextView);
				viewHolder.timeTextView = (TextView) convertView
						.findViewById(R.id.timeTextView);
				convertView.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) convertView.getTag();
			break;
		}

		Comment comment = getItem(position);
		viewHolder.bodyTextView.setText(comment.getBody());
		viewHolder.timeTextView.setText(comment.getTime());
		return convertView;
	}

	private class ViewHolder {
		public TextView bodyTextView, timeTextView;
	}

	@Override
	public int getItemViewType(int position) {
		return position % 2 == 0 ? SECTION : NORMAL;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

}
