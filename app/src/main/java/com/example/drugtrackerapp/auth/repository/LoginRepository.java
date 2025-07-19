package com.example.drugtrackerapp.auth.repository;

import com.example.drugtrackerapp.auth.model.UserInfo;
import com.example.drugtrackerapp.callback.AuthCallback;
import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.google.firebase.auth.FirebaseUser;

public class LoginRepository {

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
