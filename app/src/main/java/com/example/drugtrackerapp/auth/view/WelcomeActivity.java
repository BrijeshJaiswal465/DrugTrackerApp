package com.example.drugtrackerapp.auth.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.drugtrackerapp.base.BaseActivity;
import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends BaseActivity {
    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        //Redirection createAccount screen
        binding.btnCreateAccount.setOnClickListener(v -> redirectionActivity(CreateAccountActivity.class));
        //Redirection login screen
        binding.tvAlreadyAccount.setOnClickListener(v -> redirectionActivity(LoginActivity.class));
    }
}