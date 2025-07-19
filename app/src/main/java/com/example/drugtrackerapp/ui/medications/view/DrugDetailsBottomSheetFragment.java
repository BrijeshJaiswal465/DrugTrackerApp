package com.example.drugtrackerapp.ui.medications.view;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.FragmentDrugDetailsBottomSheetBinding;
import com.example.drugtrackerapp.ui.medications.viewModel.DrugDetailsBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class DrugDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentDrugDetailsBottomSheetBinding binding;
    private DrugDetailsBottomSheetViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_drug_details_bottom_sheet, container, false);
        viewModel = new ViewModelProvider(this).get(DrugDetailsBottomSheetViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        observeViewModel();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBottomSheetState(view);
    }

    private void observeViewModel() {

    }

    private void setBottomSheetState(View view) {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ((View) requireView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }
}