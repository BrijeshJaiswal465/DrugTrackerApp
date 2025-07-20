package com.example.drugtrackerapp.ui.medications.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.ActivityMedicationBinding;
import com.example.drugtrackerapp.ui.medications.model.DrugItem;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;
import com.example.drugtrackerapp.ui.medications.view.adapter.DrugListAdapter;
import com.example.drugtrackerapp.ui.medications.viewModel.MyMedicationViewModel;
import com.example.drugtrackerapp.utils.SwipeHelper;
import com.example.drugtrackerapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity that displays the user's saved medications.
 * Provides functionality to view the list of saved medications, delete medications via swipe,
 * and add new medications through a search interface.
 */
public class MyMedicationActivity extends AppCompatActivity implements DrugListAdapter.onItemClickListener {
    /**
     * Data binding instance for accessing the activity's views.
     */
    private ActivityMedicationBinding binding;

    /**
     * Adapter for the RecyclerView that displays the list of saved medications.
     */
    private DrugListAdapter adapter;

    /**
     * ViewModel instance that manages the medication data and operations.
     */
    private MyMedicationViewModel viewModel;

    /**
     * Initializes the activity, sets up data binding, configures the RecyclerView,
     * observes the medication list from the ViewModel, and sets up the search button.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medication);
        viewModel = new ViewModelProvider(this).get(MyMedicationViewModel.class);
        //Change status bar color
        Utility.statusBarColor(this, getColor(R.color.main_bg_color));
        //Set recyclerView
        setupRecyclerView();

        //Observe medicine list
        viewModel.getMedicineList().observe(this, medicineEntities -> {
            if (medicineEntities != null) {
                List<DrugItem> drugList = new ArrayList<>();

                for (MedicineEntity entity : medicineEntities) {
                    DrugItem item = new DrugItem(entity.getName(), "synonym", entity.getRxcui());
                    drugList.add(item);
                }

                adapter.setDrugList(drugList);
            }
        });

        // ➕ Open bottom sheet
        binding.llSearchMedications.setOnClickListener(v -> {
            SearchMedicationBottomSheetFragment fragment = new SearchMedicationBottomSheetFragment();
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });
    }


    /**
     * Initializes the RecyclerView with a LinearLayoutManager, sets up the adapter,
     * and configures the swipe-to-delete functionality using SwipeHelper.
     */
    private void setupRecyclerView() {
        adapter = new DrugListAdapter(this, true, this);
        binding.rvMedication.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMedication.setAdapter(adapter);
        //Swipe to delete functionality
        int bSize = Utility.getDimensionPixelSize(this, com.intuit.sdp.R.dimen._70sdp);
        int tSize = Utility.getDimensionPixelSize(this, com.intuit.sdp.R.dimen._12sdp);
        new SwipeHelper(this, binding.rvMedication, bSize, tSize) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.UnderlayButton> underlayButtons) {
                try {
                    //Add your delete button
                    underlayButtons.add(new SwipeHelper.UnderlayButton(getString(R.string.delete), 0, getColor(R.color.del_color), pos -> {
                        DrugItem item = adapter.getItem(pos);
                        if (item != null) {
                            // Convert DrugItem to MedicineEntity
                            MedicineEntity entity = new MedicineEntity(item.getRxcui(), item.getSynonym());
                            //Delete
                            viewModel.deleteMedicine(entity);
                            // Optionally update adapter list
                            adapter.notifyDataSetChanged();
                        }

                    }));
                } catch (Exception e) {
                    Log.e("An error occurred:", Objects.requireNonNull(e.getMessage()));
                }
            }
        };
    }


    /**
     * Handles click events on items in the medication list.
     * Currently not implemented
     *
     * @param drugItem The DrugItem that was clicked in the list
     */
    @Override
    public void onItemClick(DrugItem drugItem) {

    }
}