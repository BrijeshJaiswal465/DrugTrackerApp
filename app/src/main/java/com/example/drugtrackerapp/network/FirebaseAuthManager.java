package com.example.drugtrackerapp.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Singleton manager class for Firebase Authentication.
 * Provides centralized access to Firebase Authentication functionality throughout the app.
 * This class is initialized once in the Application class and provides static methods
 * for authentication operations.
 */
public class FirebaseAuthManager {
    /**
     * Static instance of FirebaseAuth that is initialized once and shared across the app.
     */
    private static FirebaseAuth firebaseAuth;

    /**
     * Initializes the FirebaseAuth instance.
     * This method should be called once from the Application class during app startup.
     * Ensures that the FirebaseAuth instance is only created once.
     */
    public static void init() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }

    /**
     * Provides access to the FirebaseAuth instance.
     * Used by repositories to perform authentication operations.
     * 
     * @return The initialized FirebaseAuth instance
     */
    public static FirebaseAuth getAuth() {
        return firebaseAuth;
    }

    /**
     * Retrieves the currently authenticated user.
     * 
     * @return The current FirebaseUser if authenticated, or null if not authenticated
     */
    public static FirebaseUser getCurrentUser() {
        return firebaseAuth != null ? firebaseAuth.getCurrentUser() : null;
    }

    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is authenticated, false otherwise
     */
    public static boolean isLoggedIn() {
        return firebaseAuth != null && firebaseAuth.getCurrentUser() != null;
    }
}
