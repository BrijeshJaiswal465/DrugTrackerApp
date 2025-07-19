package com.example.drugtrackerapp.auth.viewModel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drugtrackerapp.auth.model.UserInfo;
import com.example.drugtrackerapp.auth.repository.CreateAccountRepository;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.google.firebase.auth.FirebaseUser;

/**
 * ViewModel class for the Create Account screen.
 * Handles user input, validation, Firebase registration and state updates.
 */
public class CreateAccountViewModel extends ViewModel {

    // Observable user data
    public final UserInfo userInfo = new UserInfo();

    // Expose error, loading, and success states via LiveData
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<FirebaseUser> userCreated = new MutableLiveData<>();

    private final CreateAccountRepository repository = new CreateAccountRepository();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<FirebaseUser> getUserCreated() {
        return userCreated;
    }

    /**
     * Called from UI when "Register" is clicked.
     * Validates input and triggers repository registration.
     */
    public void registerUser() {
        if (!isValidate()) return;
        loading.setValue(true);
        repository.registerUser(userInfo, new AuthCallback() {
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
     * Validates user name, email and password.
     */
    private boolean isValidate() {
        if (TextUtils.isEmpty(userInfo.getName())) {
            errorMessage.setValue("Name is required");
            return false;
        } else if (TextUtils.isEmpty(userInfo.getEmail()) || !Patterns.EMAIL_ADDRESS.matcher(userInfo.getEmail()).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return false;
        } else if (TextUtils.isEmpty(userInfo.getPassword()) || userInfo.getPassword().length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}
