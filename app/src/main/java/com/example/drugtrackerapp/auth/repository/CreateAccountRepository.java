package com.example.drugtrackerapp.auth.repository;

import com.example.drugtrackerapp.auth.model.UserInfo;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountRepository {

    /**
     * Registers a new user with the given email and password using Firebase Authentication.
     * This method uses FirebaseAuthManager to get the initialized FirebaseAuth instance.
     *
     * @param userInfo An object containing user credentials such as email and password.
     * @param callback A custom AuthCallback to handle success or failure.
     */
    public void registerUser(UserInfo userInfo, AuthCallback callback) {
        FirebaseAuthManager.getAuth().createUserWithEmailAndPassword(userInfo.getEmail(), userInfo.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuthManager.getCurrentUser();
                callback.onSuccess(user);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}
