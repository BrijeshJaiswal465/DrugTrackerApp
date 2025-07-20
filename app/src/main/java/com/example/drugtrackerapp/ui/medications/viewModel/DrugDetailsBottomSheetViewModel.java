package com.example.drugtrackerapp.ui.medications.viewModel;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import com.example.drugtrackerapp.callback.CheckMedicineCallback;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;
import com.example.drugtrackerapp.ui.medications.repository.DrugDetailsBottomSheetRepository;

import java.util.List;

/**
 * ViewModel for the Drug Details bottom sheet.
 * Handles adding medications to the user's saved list with a limit check.
 * Acts as a communication layer between the UI (DrugDetailsBottomSheetFragment) and data layer (DrugDetailsBottomSheetRepository).
 */
public class DrugDetailsBottomSheetViewModel extends ViewModel {

    /**
     * Repository instance to handle database operations for medications.
     */
    private final DrugDetailsBottomSheetRepository repository;

    /**
     * Observer for the medicine list LiveData.
     * Stored as a field to allow proper cleanup in onCleared().
     */
    private Observer<List<MedicineEntity>> medicineObserver;

    /**
     * Constructor initializes the repository for database operations.
     */
    public DrugDetailsBottomSheetViewModel() {
        repository = new DrugDetailsBottomSheetRepository();
    }


    /**
     * Adds a medicine to the database with a limit check.
     * The app enforces a maximum of 7 medications per user.
     *
     * @param medicine       The MedicineEntity to be added
     * @param onSuccess      Callback to be executed when medicine is successfully added
     //* @param onLimitReached Callback to be executed when the medicine limit is reached
     */
    public void addMedicineWithLimit(MedicineEntity medicine, Runnable onSuccess, CheckMedicineCallback callback) {
        // Get current list of medicines to check against the limit
        LiveData<List<MedicineEntity>> medicinesLiveData = repository.getAllMedicines();

        // Clean up any existing observer
        if (medicineObserver != null) {
            medicinesLiveData.removeObserver(medicineObserver);
        }

        medicineObserver = new Observer<>() {
            @Override
            public void onChanged(List<MedicineEntity> medicines) {
                // Check if the medicine already exists by rxcui
                boolean alreadyExists = false;
                for (MedicineEntity m : medicines) {
                    if (m.getRxcui().equals(medicine.getRxcui())) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (alreadyExists) {
                    // Drug already added, show message and skip adding
                    callback.alreadyExists();
                } else if (medicines.size() < 7) {
                    // Add new medicine if not duplicate and limit not reached
                    addMedicine(medicine, onSuccess);
                } else {
                    // Limit reached
                    callback.limitReached();
                }

                // Always remove observer after work is done
                medicinesLiveData.removeObserver(this);
                medicineObserver = null;
            }
        };

        medicinesLiveData.observeForever(medicineObserver);
    }

    /**
     * Private helper method to add a medicine to the database.
     * Delegates the database operation to the repository.
     *
     * @param entity    The MedicineEntity to be added
     * @param onSuccess Callback to be executed when medicine is successfully added
     */
    private void addMedicine(MedicineEntity entity, Runnable onSuccess) {
        repository.insertMedicine(entity, onSuccess);
    }

    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * This is the place to clean up any resources, including removing observers to prevent memory leaks.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up any existing observer
        if (medicineObserver != null) {
            repository.getAllMedicines().removeObserver(medicineObserver);
            medicineObserver = null;
        }
    }
}