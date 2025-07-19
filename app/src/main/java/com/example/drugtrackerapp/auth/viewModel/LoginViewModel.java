package com.example.drugtrackerapp.auth.viewModel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drugtrackerapp.auth.model.UserInfo;
import com.example.drugtrackerapp.auth.repository.LoginRepository;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    // Observable user data
    public final UserInfo userInfo = new UserInfo();

    // Expose error, loading, and success states via LiveData
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<FirebaseUser> userCreated = new MutableLiveData<>();

    private final LoginRepository repository = new LoginRepository();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<FirebaseUser> getUserLogin() {
        return userCreated;
    }

    /**
     * Called from UI when "Register" is clicked.
     * Validates input and triggers repository registration.
     */
    public void loginUser() {
        if (!isValidate()) return;
        loading.setValue(true);
        repository.loginUser(userInfo, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loading.setValue(false);
                userCreated.setValue(user); // expose success
            }

            @Override
            public void onFailure(Exception e) {
                loading.setValue(false);
                errorMessage.setValue(e.getMessage()); // expose error
            }
        });
    }

    /**
     * Validates email and password.
     */
    private boolean isValidate() {
        if (TextUtils.isEmpty(userInfo.getEmail()) || !Patterns.EMAIL_ADDRESS.matcher(userInfo.getEmail()).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return false;
        } else if (TextUtils.isEmpty(userInfo.getPassword()) || userInfo.getPassword().length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}
