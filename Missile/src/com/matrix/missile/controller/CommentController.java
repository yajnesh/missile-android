package com.matrix.missile.controller;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.missile.model.Comment;
import com.matrix.missile.util.MissileRestClient;

public class CommentController extends JsonHttpResponseHandler {
	private static final String PARAM_MISSILE_ID = "missile_id",
			PARAM_BODY = "body";
	private int mMissileId;
	private Context context;
	private CommentControllerInterface mCommentControllerInterface;

	public CommentController(Context context,
			CommentControllerInterface commentControllerInterface, int missileId) {
		this.context = context;
		mMissileId = missileId;
		mCommentControllerInterface = commentControllerInterface;
	}

	public void createComment(String body) {
		JSONObject jsonParams = new JSONObject();
		StringEntity entity = null;
		try {
			jsonParams.put(PARAM_MISSILE_ID, Integer.toString(mMissileId));
			jsonParams.put(PARAM_BODY, body);
			entity = new StringEntity(jsonParams.toString());
		} catch (JSONException e) {
		} catch (UnsupportedEncodingException e) {
		}
		MissileRestClient.post(context, "comments.json", entity, this);
	}

	public void getComments() {
		RequestParams params = new RequestParams();
		params.put(PARAM_MISSILE_ID, mMissileId + "");
		MissileRestClient.get("comments.json", params, this);
	}

	@Override
	public void onSuccess(JSONArray response) {
		super.onSuccess(response);
		Gson gson = new Gson();
		Comment[] comments = gson
				.fromJson(response.toString(), Comment[].class);
		mCommentControllerInterface.onLoadingFinsh(comments);
	}

	@Override
	public void onSuccess(int statusCode, JSONObject response) {
		super.onSuccess(statusCode, response);
		Gson gson = new Gson();
		Comment comment = gson.fromJson(response.toString(), Comment.class);
		mCommentControllerInterface.onCommetCreated(comment);
	}

	public static interface CommentControllerInterface extends InternetLoader {
		public void onCommetCreated(Comment comment);
	}
}
