package com.example.drugtrackerapp.auth.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.auth.viewModel.CreateAccountViewModel;
import com.example.drugtrackerapp.auth.viewModel.LoginViewModel;
import com.example.drugtrackerapp.base.BaseActivity;
import com.example.drugtrackerapp.databinding.ActivityLoginBinding;
import com.example.drugtrackerapp.utils.Utility;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //Init ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                redirectionActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return Utility.onTouchEventHideKeyBoard(this);
    }
}