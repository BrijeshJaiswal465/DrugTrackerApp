package com.example.drugtrackerapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewSfBold extends AppCompatTextView {

    public CustomTextViewSfBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utility.setFontSfBoldText(context));
    }

    public CustomTextViewSfBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextViewSfBold(Context context) {
        super(context);
    }
}