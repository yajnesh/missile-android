package com.matrix.missile.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.matrix.missile.R;
import com.matrix.missile.util.MissileConstance;

public class CustomeCheckBox extends CheckBox {

	public CustomeCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public CustomeCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomViewDefault);
		String font = MissileConstance.DEFAULT_FONT;
		if (a.getString(R.styleable.CustomViewDefault_isItalic) != null)
			font = MissileConstance.DEFAULT_FONT_ITALIC;
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
		setTypeface(tf);
		a.recycle();
	}
}