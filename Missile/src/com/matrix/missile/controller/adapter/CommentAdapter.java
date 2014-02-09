package com.matrix.missile.controller.adapter;

import com.matrix.missile.R;
import com.matrix.missile.model.Comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommentAdapter extends SupportArrayAdapter<Comment> {

	public CommentAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.comment_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.bodyTextView = (TextView) convertView
					.findViewById(R.id.bodyTextView);
			viewHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.timeTextView);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		Comment comment = getItem(position);
		viewHolder.bodyTextView.setText(comment.getBody());
		viewHolder.timeTextView.setText(comment.getTime());
		return convertView;
	}

	private class ViewHolder {
		public TextView bodyTextView, timeTextView;
	}
}
