package com.example.drugtrackerapp.ui.medications.repository;

import androidx.lifecycle.LiveData;

import com.example.drugtrackerapp.base.DrugTrackerApp;
import com.example.drugtrackerapp.ui.medications.model.MedicineDao;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Repository class that handles database operations for the drug details bottom sheet.
 * Acts as an abstraction layer between the ViewModel and the Room database.
 * This class is responsible for adding new medications to the database.
 */
public class DrugDetailsBottomSheetRepository {

    /**
     * Data Access Object for medicine database operations.
     */
    private final MedicineDao dao;

    /**
     * Constructor that initializes the MedicineDao from the application's database instance.
     */
    public DrugDetailsBottomSheetRepository() {
        dao = DrugTrackerApp.database.medicineDao();
    }

    /**
     * Inserts a new medicine into the database.
     * This operation is performed on a background thread to avoid blocking the UI thread.
     * After successful insertion, the onSuccess callback is executed if provided.
     * 
     * @param entity The MedicineEntity to be inserted
     * @param onSuccess Optional callback to be executed after successful insertion
     */
    public void insertMedicine(MedicineEntity entity, Runnable onSuccess) {
        Executors.newSingleThreadExecutor().execute(() -> {
            dao.insertMedicine(entity);
            if (onSuccess != null) onSuccess.run();
        });
    }

    /**
     * Retrieves all saved medicines from the database.
     * The result is wrapped in LiveData to allow the UI to observe changes.
     * This method is used to check the current count of saved medicines.
     * 
     * @return LiveData containing a list of all saved MedicineEntity objects
     */
    public LiveData<List<MedicineEntity>> getAllMedicines() {
        return dao.getAllMedicines();
    }
}

