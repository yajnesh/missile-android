package com.matrix.missile.model;

import com.matrix.missile.util.Util;

public class Comment {
	private String body, created_at;

	public Comment(String body, String time) {
		setBody(body);
		setTime(time);
	}

	public String getTime() {
		return Util.getHumanReadableUpdateAt(created_at);
	}

	public void setTime(String time) {
		created_at = time;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
