package com.matrix.missile.view.customviews.component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.matrix.missile.view.customviews.CustomeTextView;

public class LinkEnabledTextView extends CustomeTextView {

	private ArrayList<Hyperlink> listOfLinks;
	TextLinkClickListener mListener;

	// Pattern for gathering #hasttags from the Text
	Pattern hashTagsPattern = Pattern.compile("(#[^ ]+)");

	public LinkEnabledTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		listOfLinks = new ArrayList<Hyperlink>();

	}

	private void gatherLinksForText(String text) {
		SpannableString linkableText = new SpannableString(text);
		// gatherLinks basically collects the Links depending upon the Pattern
		// that we supply
		// and add the links to the ArrayList of the links

		gatherLinks(listOfLinks, linkableText, hashTagsPattern);

		for (int i = 0; i < listOfLinks.size(); i++) {
			Hyperlink linkSpec = listOfLinks.get(i);
			// this process here makes the Clickable Links from the text

			try {
				linkableText.setSpan(linkSpec.span, linkSpec.start,
						linkSpec.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {

				Log.e("TAG",linkSpec.span+" "+linkSpec.start+ " " + linkSpec.end+ " " + linkableText.length() );
			}
		};

		// sets the text for the TextView with enabled links

		super.setText(linkableText);
	}

	// sets the Listener for later click propagation purpose

	public void setOnTextLinkClickListener(TextLinkClickListener newListener) {
		mListener = newListener;
	}

	// The Method mainly performs the Regex Comparison for the Pattern and adds
	// them to
	// listOfLinks array list

	private final void gatherLinks(ArrayList<Hyperlink> links, Spannable s,
			Pattern pattern) {
		// Matcher matching the pattern
		Matcher m = pattern.matcher(s);

		while (m.find()) {
			int start = m.start();
			int end = m.end();

			// Hyperlink is basically used like a structure for storing the
			// information about
			// where the link was found.

			Hyperlink spec = new Hyperlink();

			spec.textSpan = s.subSequence(start, end);
			spec.span = new InternalURLSpan(spec.textSpan.toString());
			spec.start = start;
			spec.end = end;

			links.add(spec);
		}
	}

	// This is class which gives us the clicks on the links which we then can
	// use.

	public class InternalURLSpan extends ClickableSpan {
		private String clickedSpan;

		public InternalURLSpan(String clickedString) {
			clickedSpan = clickedString;
		}

		@Override
		public void onClick(View textView) {
			if (mListener != null)
				mListener.onTextLinkClick(textView, clickedSpan);
		}
	}

	// Class for storing the information about the Link Location

	private class Hyperlink {
		CharSequence textSpan;
		InternalURLSpan span;
		int start;
		int end;
	}

	public void setText(String text) {
		this.gatherLinksForText(text);

		MovementMethod m = this.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			if (this.getLinksClickable()) {
				this.setMovementMethod(LinkMovementMethod.getInstance());
			}
		}
		Linkify.addLinks(this, Linkify.ALL);

	}

	public static interface TextLinkClickListener {

		/*
		 * This method is called when the TextLink is clicked from
		 * LinkEnabledTextView
		 */
		public void onTextLinkClick(View textView, String clickedString);
	}
}