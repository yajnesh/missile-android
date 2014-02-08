package com.matrix.missile.component;
import android.view.View;

public interface TextLinkClickListener
{

 /*
  *  This method is called when the TextLink is clicked from LinkEnabledTextView
  */
public void onTextLinkClick(View textView, String clickedString);
}