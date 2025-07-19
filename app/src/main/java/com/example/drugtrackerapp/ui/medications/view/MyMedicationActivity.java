package com.example.drugtrackerapp.ui.medications.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.ActivityMedicationBinding;

public class MyMedicationActivity extends AppCompatActivity {

    private ActivityMedicationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medication);

        //Open bottom add medication fragment bottom sheet
        binding.llSearchMedications.setOnClickListener(v -> {
            SearchMedicationBottomSheetFragment fragment = new SearchMedicationBottomSheetFragment();
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });

    }
}