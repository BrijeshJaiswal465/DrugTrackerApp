package com.example.drugtrackerapp.ui.auth.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.drugtrackerapp.base.BaseActivity;
import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.ActivityWelcomeBinding;
import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.example.drugtrackerapp.ui.medications.view.MyMedicationActivity;
import com.example.drugtrackerapp.utils.Utility;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelcomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        //Change status bar color
        Utility.statusBarColor(this, getColor(R.color.very_light_periwinkle));
        //Redirection createAccount screen
        binding.btnCreateAccount.setOnClickListener(v -> redirectionActivity(CreateAccountActivity.class));
        //Redirection login screen
        binding.tvAlreadyAccount.setOnClickListener(v -> redirectionActivity(LoginActivity.class));

        //Check user is logged in
        if (FirebaseAuthManager.isLoggedIn()) {
            startActivityWithFinishStack(this, MyMedicationActivity.class);
        }
    }
}