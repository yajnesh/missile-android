package com.matrix.missile.model;

import com.matrix.missile.util.Util;

import android.os.Parcel;
import android.os.Parcelable;

public class Missile implements Parcelable {
	private int id;
	private String title;
	private String message;
	private String created_at;

	public Missile() {

	}

	public Missile(Parcel in) {
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		message = in.readString();
		created_at = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(message);
		dest.writeString(created_at);
	}

	public static final Parcelable.Creator<Missile> CREATOR = new Parcelable.Creator<Missile>() {

		public Missile createFromParcel(Parcel in) {
			return new Missile(in);
		}

		public Missile[] newArray(int size) {
			return new Missile[size];
		}

	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return Util.getHumanReadableUpdateAt(created_at);
	}

	public void setTime(String time) {
		this.created_at = time;
	}

	@Override
	public String toString() {
		return "title:" + title + ",Message:" + message + ";";
	}
}
