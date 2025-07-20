package com.example.drugtrackerapp.ui.medications.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Entity class representing a medicine in the database.
 * This class defines the structure of the 'medicines' table in the Room database.
 * Each instance of this class represents a row in the table.
 */
@Entity(tableName = "medicines")
public class MedicineEntity {
    /**
     * The RxCUI (RxNorm Concept Unique Identifier) of the medicine.
     * This is a unique identifier for the medicine and serves as the primary key in the database.
     * It cannot be null as indicated by the @NonNull annotation.
     */
    @PrimaryKey
    @NonNull
    public String rxcui;
    
    /**
     * The name of the medicine.
     * This field stores the display name of the medication.
     */
    public String name;

    /**
     * Constructor for creating a new MedicineEntity.
     * 
     * @param rxcui The unique RxCUI identifier for the medicine, cannot be null
     * @param name The display name of the medicine
     */
    public MedicineEntity(@NonNull String rxcui, String name) {
        this.rxcui = rxcui;
        this.name = name;
    }

    /**
     * Gets the RxCUI of the medicine.
     * 
     * @return The RxCUI identifier, never null
     */
    @NonNull
    public String getRxcui() {
        return rxcui;
    }


    /**
     * Gets the name of the medicine.
     * 
     * @return The name of the medicine
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medicine.
     * 
     * @param name The new name for the medicine
     */
    public void setName(String name) {
        this.name = name;
    }
}

