package com.example.drugtrackerapp.base;

import android.os.Bundle;

import com.example.drugtrackerapp.utils.ProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetBaseFragment extends BottomSheetDialogFragment {

    private ProgressDialog progressDialogs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for null context to avoid crash
        if (getContext() != null) {
            progressDialogs = new ProgressDialog(getContext());
        }
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
