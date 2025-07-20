package com.example.drugtrackerapp.ui.auth.repository;

import com.example.drugtrackerapp.ui.auth.model.UserInfo;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.google.firebase.auth.FirebaseUser;

/**
 * Repository class that handles user login operations.
 * Acts as an abstraction layer between the ViewModel and Firebase Authentication.
 * This class is responsible for making the actual authentication calls to Firebase.
 */
public class LoginRepository {

    /**
     * Authenticates a user with email and password using Firebase Authentication.
     * This method uses FirebaseAuthManager to get the initialized FirebaseAuth instance.
     *
     * @param userInfo An object containing user credentials (email and password)
     * @param callback A callback interface to handle authentication results asynchronously
     * onSuccess is called with the FirebaseUser when authentication succeeds
     * onFailure is called with the Exception when authentication fails
     */
    public void loginUser(UserInfo userInfo, AuthCallback callback) {
        FirebaseAuthManager.getAuth().signInWithEmailAndPassword(userInfo.getEmail(), userInfo.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuthManager.getCurrentUser();
                callback.onSuccess(user);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}
