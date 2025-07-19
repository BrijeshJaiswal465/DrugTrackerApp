package com.example.drugtrackerapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewNhuMedium extends AppCompatTextView {

    public CustomTextViewNhuMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Utility.setFontNhuMediumText(context));
    }

    public CustomTextViewNhuMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextViewNhuMedium(Context context) {
        super(context);
    }
}