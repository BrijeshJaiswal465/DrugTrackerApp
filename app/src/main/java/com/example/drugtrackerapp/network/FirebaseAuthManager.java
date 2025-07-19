package com.example.drugtrackerapp.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {
    private static FirebaseAuth firebaseAuth;

    // Initialize once (from Application class)
    public static void init() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }

    public static FirebaseAuth getAuth() {
        return firebaseAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return firebaseAuth != null ? firebaseAuth.getCurrentUser() : null;

    }

    public static boolean isLoggedIn() {
        return firebaseAuth != null && firebaseAuth.getCurrentUser() != null;

    }

    public static void signOut() {
        firebaseAuth.signOut();
    }
}
