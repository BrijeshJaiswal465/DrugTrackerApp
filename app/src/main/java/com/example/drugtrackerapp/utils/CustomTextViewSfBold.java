package com.example.drugtrackerapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * A custom TextView that automatically applies the SF Bold typeface.
 * This class extends AppCompatTextView and sets the SF Bold font from the Utility class.
 */
public class CustomTextViewSfBold extends AppCompatTextView {

    /**
     * Constructor used when creating the view from XML.
     * Automatically applies the SF Bold typeface to the TextView.
     * 
     * @param context The Context the view is running in
     * @param attrs The attributes of the XML tag that is inflating the view
     */
    public CustomTextViewSfBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utility.setFontSfBoldText(context));
    }

    /**
     * Constructor used when creating the view from XML with a specified style.
     * 
     * @param context The Context the view is running in
     * @param attrs The attributes of the XML tag that is inflating the view
     * @param defStyle The default style to apply to this view
     */
    public CustomTextViewSfBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Simple constructor to use when creating a view from code.
     * 
     * @param context The Context the view is running in
     */
    public CustomTextViewSfBold(Context context) {
        super(context);
    }
}