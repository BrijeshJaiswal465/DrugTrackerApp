package com.example.drugtrackerapp.ui.medications.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for the MedicineEntity.
 * This interface defines the database operations that can be performed on the medicines table.
 * Room automatically generates the implementation of this interface at compile time.
 */
@Dao
public interface MedicineDao {

    /**
     * Inserts a medicine entity into the database.
     * If a medicine with the same rxcui already exists, it will be replaced with the new one
     * due to the REPLACE conflict strategy.
     *
     * @param medicine The MedicineEntity to be inserted or updated
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicine(MedicineEntity medicine);

    /**
     * Retrieves all medicines from the database as a LiveData object.
     * This allows the UI to automatically update when the data changes.
     *
     * @return LiveData containing a list of all MedicineEntity objects in the database
     */
    @Query("SELECT * FROM medicines")
    LiveData<List<MedicineEntity>> getAllMedicines();

    /**
     * Deletes a specific medicine entity from the database.
     * This method requires the complete MedicineEntity object to be passed.
     *
     * @param medicine The MedicineEntity to be deleted
     */
    @Delete
    void deleteMedicine(MedicineEntity medicine);
}
