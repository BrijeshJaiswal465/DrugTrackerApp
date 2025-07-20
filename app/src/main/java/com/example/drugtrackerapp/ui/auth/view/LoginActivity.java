package com.example.drugtrackerapp.ui.auth.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.ui.auth.viewModel.LoginViewModel;
import com.example.drugtrackerapp.base.BaseActivity;
import com.example.drugtrackerapp.databinding.ActivityLoginBinding;
import com.example.drugtrackerapp.ui.medications.view.MyMedicationActivity;
import com.example.drugtrackerapp.utils.Utility;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //Change status bar color
        Utility.statusBarColor(this, getColor(R.color.white));
        //Init ViewModel
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.setUserInfo(viewModel.userInfo);

        //Bind ViewModel to layout
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);


        // Observe error message
        viewModel.getErrorMessage().observe(this, message -> Utility.alerter(this, message));

        // Observe loading state (e.g., show/hide progress bar)
        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) showLoader();
                else hideLoader();
            }
        });

        // Observe successful registration
        viewModel.getUserLogin().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                startActivityWithFinishStack(this, MyMedicationActivity.class);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return Utility.onTouchEventHideKeyBoard(this);
    }
}