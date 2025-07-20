package com.example.drugtrackerapp.ui.medications.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;
import com.example.drugtrackerapp.ui.medications.repository.MyMedicationRepository;

import java.util.List;

/**
 * ViewModel for the My Medications screen.
 * Manages the list of user's saved medications and provides methods to interact with them.
 * Acts as a communication layer between the UI (MyMedicationActivity) and data layer (MyMedicationRepository).
 */
public class MyMedicationViewModel extends ViewModel {

    /**
     * Repository instance to handle database operations for medications.
     */
    private final MyMedicationRepository repository;
    
    /**
     * LiveData containing the list of all saved medications.
     * This is automatically updated when the underlying database changes.
     */
    private final LiveData<List<MedicineEntity>> medicineList;

    /**
     * Constructor initializes the repository and retrieves the list of medications.
     * The medication list is loaded once and will be automatically updated when the database changes.
     */
    public MyMedicationViewModel() {
        repository = new MyMedicationRepository();
        medicineList = repository.getAllMedicines();
    }

    /**
     * Provides the list of medications as LiveData to the UI.
     * @return LiveData<List<MedicineEntity>> containing all saved medications
     */
    public LiveData<List<MedicineEntity>> getMedicineList() {
        return medicineList;
    }

    /**
     * Deletes a medication from the database.
     * This operation is performed asynchronously by the repository.
     * @param entity The MedicineEntity to be deleted
     */
    public void deleteMedicine(MedicineEntity entity) {
        repository.deleteMedicine(entity);
    }
}