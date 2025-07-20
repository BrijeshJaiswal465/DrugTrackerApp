package com.example.drugtrackerapp.ui.medications.view;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.callback.CheckMedicineCallback;
import com.example.drugtrackerapp.databinding.FragmentDrugDetailsBottomSheetBinding;
import com.example.drugtrackerapp.ui.medications.model.DrugItem;
import com.example.drugtrackerapp.ui.medications.model.MedicineEntity;
import com.example.drugtrackerapp.ui.medications.viewModel.DrugDetailsBottomSheetViewModel;
import com.example.drugtrackerapp.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A bottom sheet fragment that displays detailed information about a selected drug/medication.
 * This fragment allows users to view drug details and add the medication to their saved list.
 * It receives a DrugItem object through arguments and displays its information.
 */
public class DrugDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    /**
     * The drug item to display details for, passed via fragment arguments.
     */
    private DrugItem drugItem;

    /**
     * Initializes the fragment and retrieves the DrugItem from arguments.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drugItem = (DrugItem) getArguments().getSerializable("DrugItem");
        }
    }

    /**
     * Creates and configures the view for this fragment.
     * Sets up data binding, initializes the ViewModel, and configures UI elements including
     * the medication details and the Add Medicine button functionality.
     *
     * @param inflater           The LayoutInflater object to inflate views
     * @param container          The parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDrugDetailsBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_drug_details_bottom_sheet, container, false);
        DrugDetailsBottomSheetViewModel viewModel = new ViewModelProvider(this).get(DrugDetailsBottomSheetViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Set text
        binding.includeBottomSheetHeader.tvTitle.setText(R.string.details);
        //Close
        binding.includeBottomSheetHeader.llBack.setOnClickListener(v -> dismiss());
        //Set details
        binding.tvDetails.setText(Html.fromHtml(Utility.getDummyDetails(), Html.FROM_HTML_MODE_LEGACY));

        if (drugItem != null) {
            String name = drugItem.getSynonym();
            binding.tvMedicationName.setText(name.isEmpty() ? "N/A" : name);
        }

        binding.btnAddMedicine.setOnClickListener(v -> {
            if (drugItem != null) {
                MedicineEntity entity = new MedicineEntity(drugItem.getRxcui(), drugItem.getSynonym());

                viewModel.addMedicineWithLimit(entity,
                        () -> {
                            if (isAdded()) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), getString(R.string.medicine_added_successfully), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                });
                            }
                        }, new CheckMedicineCallback() {
                            @Override
                            public void alreadyExists() {
                                showErrorMessage(getString(R.string.drug_already_added));
                            }

                            @Override
                            public void limitReached() {
                                showErrorMessage(getString(R.string.you_can_only_add_up_to_7_medicines));
                            }
                        }
                );
            }
        });

        return binding.getRoot();
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored
     * in to the view. Sets the bottom sheet to expanded state.
     *
     * @param view               The View returned by onCreateView()
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBottomSheetState(view);
    }

    /**
     * Configures the bottom sheet behavior to be fully expanded when shown
     * and sets its background to transparent.
     *
     * @param view The root view of the fragment
     */
    private void setBottomSheetState(View view) {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ((View) requireView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    private void showErrorMessage(String message) {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
        }
    }
}