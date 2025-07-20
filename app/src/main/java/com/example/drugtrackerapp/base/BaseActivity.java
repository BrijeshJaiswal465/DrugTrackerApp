package com.example.drugtrackerapp.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugtrackerapp.utils.ProgressDialog;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialogs = new ProgressDialog(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Redirection helper with fade animation
     */
    public void redirectionActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /***
     * We are using this function clear all stack activity with intent
     */
    public void startActivityWithFinishStack(Activity currentActivity, Class<?> targetActivityClass) {
        Intent intent = new Intent(currentActivity, targetActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(intent);
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
