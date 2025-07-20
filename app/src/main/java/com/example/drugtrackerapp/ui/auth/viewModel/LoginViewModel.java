package com.example.drugtrackerapp.ui.auth.viewModel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drugtrackerapp.ui.auth.model.UserInfo;
import com.example.drugtrackerapp.ui.auth.repository.LoginRepository;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.google.firebase.auth.FirebaseUser;

/**
 * ViewModel for the Login screen.
 * Handles user authentication logic, input validation, and state management.
 * Acts as a communication layer between the UI (LoginActivity) and data layer (LoginRepository).
 */
public class LoginViewModel extends ViewModel {

    /**
     * Observable user data for two-way data binding with the login form.
     * Contains email and password fields that are updated as the user types.
     */
    public final UserInfo userInfo = new UserInfo();

    /**
     * LiveData to expose error messages to the UI.
     * Updated during validation or when login fails.
     */
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    /**
     * LiveData to track the loading state during authentication.
     * Used to show/hide loading indicators in the UI.
     */
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    
    /**
     * LiveData to expose the authenticated Firebase user to the UI.
     * Updated when login is successful.
     */
    private final MutableLiveData<FirebaseUser> userCreated = new MutableLiveData<>();

    /**
     * Repository instance to handle Firebase authentication operations.
     */
    private final LoginRepository repository = new LoginRepository();

    /**
     * Provides error messages as LiveData to the UI.
     * @return LiveData<String> containing validation or authentication error messages
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Provides the loading state as LiveData to the UI.
     * @return LiveData<Boolean> indicating whether a loading operation is in progress
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Provides the authenticated user as LiveData to the UI.
     * @return LiveData<FirebaseUser> containing the authenticated Firebase user when login is successful
     */
    public LiveData<FirebaseUser> getUserLogin() {
        return userCreated;
    }

    /**
     * Called from UI when the login button is clicked.
     * Validates user input and triggers the login process via the repository.
     * Updates loading state and handles success/failure callbacks.
     */
    public void loginUser() {
        // Validate input before proceeding
        if (!isValidate()) return;
        
        // Set loading state to true to show loading indicator
        loading.setValue(true);
        
        // Call repository to perform Firebase authentication
        repository.loginUser(userInfo, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                // Hide loading indicator
                loading.setValue(false);
                // Expose successful login by updating the user LiveData
                userCreated.setValue(user);
            }

            @Override
            public void onFailure(Exception e) {
                // Hide loading indicator
                loading.setValue(false);
                // Expose error message from Firebase
                errorMessage.setValue(e.getMessage());
            }
        });
    }

    /**
     * Validates the email and password input.
     * Checks for empty fields, valid email format, and minimum password length.
     * Updates the errorMessage LiveData with appropriate validation messages.
     * 
     * @return boolean indicating whether validation passed (true) or failed (false)
     */
    private boolean isValidate() {
        // Validate email format
        if (TextUtils.isEmpty(userInfo.getEmail()) || !Patterns.EMAIL_ADDRESS.matcher(userInfo.getEmail()).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return false;
        } 
        // Validate password length (Firebase requires at least 6 characters)
        else if (TextUtils.isEmpty(userInfo.getPassword()) || userInfo.getPassword().length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}
