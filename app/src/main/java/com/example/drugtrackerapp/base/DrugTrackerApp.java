package com.example.drugtrackerapp.base;

import android.app.Application;

import androidx.room.Room;

import com.example.drugtrackerapp.network.FirebaseAuthManager;
import com.example.drugtrackerapp.ui.medications.database.AppDatabase;
import com.google.firebase.FirebaseApp;

public class DrugTrackerApp extends Application {
    public static AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize firebase
        FirebaseApp.initializeApp(this);
        //Initialize Firebase Auth
        FirebaseAuthManager.init();
        //Room database
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "drug_tracker_db"
        ).build();
    }
}