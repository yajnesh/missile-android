package com.matrix.missile.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matrix.missile.R;
import com.matrix.missile.controller.CommentController;
import com.matrix.missile.controller.CommentController.CommentControllerInterface;
import com.matrix.missile.controller.adapter.CommentAdapter;
import com.matrix.missile.controller.tag.TagController;
import com.matrix.missile.model.Comment;
import com.matrix.missile.model.Missile;
import com.matrix.missile.view.customviews.component.LinkEnabledTextView;

public class MissileFragment extends ListFragment implements OnClickListener,
		CommentControllerInterface {
	private TextView tvTitle, tvTime;
	private LinkEnabledTextView tvMessage;
	private Missile mMissile;
	private View rootView;
	private EditText commentEditText;
	private ImageButton submitImageButton;
	private CommentAdapter mAdapter;
	private Handler mHandler;
	private int mInterval = 10000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_missile_layout,
				container, false);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		mMissile = getArguments().getParcelable("missile");
		initViews();
		ViewMissilesFragment viewMissilesActivity = new ViewMissilesFragment();
		tvMessage.setOnTextLinkClickListener(new TagController(
				getFragmentManager(), viewMissilesActivity, this));
		startRepeatingTask();
	}

	private void initViews() {
		initHeaderView();
		initFooterView();
		initListView();
	}

	private void initFooterView() {
		View footerView = getActivity().getLayoutInflater().inflate(
				R.layout.comment_view, null);
		getListView().addFooterView(footerView);
		commentEditText = (EditText) footerView
				.findViewById(R.id.commentEditText);
		submitImageButton = (ImageButton) footerView
				.findViewById(R.id.submitImageButton);
		submitImageButton.setOnClickListener(this);
	}

	private void initHeaderView() {
		View headerView = getActivity().getLayoutInflater().inflate(
				R.layout.missile_view, null);
		getListView().addHeaderView(headerView);
		tvTitle = (TextView) headerView.findViewById(R.id.tvTitle);
		tvTime = (TextView) headerView.findViewById(R.id.tvTime);
		tvMessage = (LinkEnabledTextView) headerView
				.findViewById(R.id.tvMessage);
		tvTitle.setText(mMissile.getTitle());
		tvMessage.setText(mMissile.getMessage());
		tvTime.setText(mMissile.getTime());
	}

	private void initListView() {
		mAdapter = new CommentAdapter(getActivity());
		setListAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		String bodyString = commentEditText.getText().toString();
		if (bodyString == null || bodyString.equals("")) {
			commentEditText.setError("Comment can't be blank");
			return;
		} else
			commentEditText.setError(null);
		new CommentController(getActivity(), this, mMissile.getId())
				.createComment(bodyString);
		commentEditText.setText("");
	}

	@Override
	public void onLoadingFinsh(Comment[] comments) {
		mAdapter.clear();
		mAdapter.supportAddAll(comments);
	}

	@Override
	public void onLoadingStarted() {
	}

	@Override
	public void onLoadingFailier() {
	}

	@Override
	public void onCommetCreated(Comment comment) {
		mAdapter.add(comment);
	}

	private Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			new CommentController(getActivity(), MissileFragment.this,
					mMissile.getId()).getComments();
			mHandler.postDelayed(mStatusChecker, mInterval);
		}
	};

	void startRepeatingTask() {
		new CommentController(getActivity(), this, mMissile.getId())
				.getComments();
		mStatusChecker.run();
	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
	}
}
