package com.example.drugtrackerapp.ui.medications.repository;

import androidx.lifecycle.LiveData;

import com.example.drugtrackerapp.base.DrugTrackerApp;
import com.example.drugtrackerapp.ui.medications.model.MedicineDao;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Repository class that handles database operations for the user's saved medications.
 * Acts as an abstraction layer between the ViewModel and the Room database.
 * This class is responsible for retrieving and modifying medication data.
 */
public class MyMedicationRepository {

    /**
     * Data Access Object for medicine database operations.
     */
    private final MedicineDao dao;

    /**
     * Constructor that initializes the MedicineDao from the application's database instance.
     */
    public MyMedicationRepository() {
        dao = DrugTrackerApp.database.medicineDao();
    }

    /**
     * Retrieves all saved medicines from the database.
     * The result is wrapped in LiveData to allow the UI to observe changes.
     * 
     * @return LiveData containing a list of all saved MedicineEntity objects
     */
    public LiveData<List<MedicineEntity>> getAllMedicines() {
        return dao.getAllMedicines();
    }

    /**
     * Deletes a medicine from the database.
     * This operation is performed on a background thread to avoid blocking the UI thread.
     * 
     * @param entity The MedicineEntity to be deleted
     */
    public void deleteMedicine(MedicineEntity entity) {
        Executors.newSingleThreadExecutor().execute(() -> dao.deleteMedicine(entity));
    }
}