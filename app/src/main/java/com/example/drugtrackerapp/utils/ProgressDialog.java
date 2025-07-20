package com.example.drugtrackerapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.drugtrackerapp.R;

/**
 * A custom progress dialog that displays a loading indicator.
 * This dialog has a transparent background and cannot be canceled by touching outside
 * or by pressing the back button.
 */
public class ProgressDialog extends Dialog {
    /**
     * Creates a new ProgressDialog instance.
     * 
     * @param context the context in which the dialog should appear
     */
    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    /**
     * Initializes the dialog when it is first created.
     * Sets up the dialog with no title, custom layout, transparent background,
     * and prevents cancellation by touch or back button.
     * 
     * @param savedInstanceState the saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.blue_progress_bar);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
