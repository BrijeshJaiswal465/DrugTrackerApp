package com.example.drugtrackerapp.base;

import android.app.Application;

import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.google.firebase.FirebaseApp;

public class DrugTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize firebase
        FirebaseApp.initializeApp(this);
        //Initialize Firebase Auth
        FirebaseAuthManager.init();
    }
}