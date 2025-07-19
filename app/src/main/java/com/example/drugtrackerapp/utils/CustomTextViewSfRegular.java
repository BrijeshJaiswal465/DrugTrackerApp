package com.example.drugtrackerapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewSfRegular extends AppCompatTextView {

    public CustomTextViewSfRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utility.setFontSfRegularText(context));
    }

    public CustomTextViewSfRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextViewSfRegular(Context context) {
        super(context);
    }
}