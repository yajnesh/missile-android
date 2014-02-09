package com.matrix.missile.controller;

import com.matrix.missile.model.Comment;

public interface InternetLoader {
	public void onLoadingFinsh(Comment[] comments);

	public void onLoadingStarted();

	public void onLoadingFailier();
}
