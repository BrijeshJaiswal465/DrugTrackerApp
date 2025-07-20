package com.example.drugtrackerapp.ui.medications.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.drugtrackerapp.ui.medications.model.MedicineDao;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;

/**
 * Room Database class for the application.
 * This abstract class defines the database configuration and serves as the main access point
 * for the underlying connection to the SQLite database.
 * The @Database annotation specifies the entities that belong to the database and the version number.
 * Migrations can be added when the version number is incremented.
 * exportSchema is set to false to avoid exporting the schema to a folder.
 */
@Database(entities = {MedicineEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Provides access to the MedicineDao interface for database operations.
     * This method is abstract and Room automatically generates its implementation.
     * 
     * @return An implementation of the MedicineDao interface for accessing the medicines table
     */
    public abstract MedicineDao medicineDao();
}