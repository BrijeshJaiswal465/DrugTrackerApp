package com.example.drugtrackerapp.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugtrackerapp.utils.ProgressDialog;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialogs = new ProgressDialog(this);
    }

    /**
     * Redirection helper with fade animation
     */
    public void redirectionActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Show loading dialog
     */
    public void showLoader() {
        if (progressDialogs != null && !progressDialogs.isShowing()) {
            progressDialogs.show();
        }
    }

    /**
     * Hide loading dialog
     */
    public void hideLoader() {
        if (progressDialogs != null && progressDialogs.isShowing()) {
            progressDialogs.dismiss();
        }
    }
}
