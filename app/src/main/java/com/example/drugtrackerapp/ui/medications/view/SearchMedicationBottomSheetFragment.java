package com.example.drugtrackerapp.ui.medications.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.FragmentSearchMedicationBinding;
import com.example.drugtrackerapp.ui.medications.model.DrugItem;
import com.example.drugtrackerapp.ui.medications.viewModel.SearchMedicationViewModel;
import com.example.drugtrackerapp.utils.SwipeHelper;
import com.example.drugtrackerapp.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchMedicationBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentSearchMedicationBinding binding;
    private SearchMedicationViewModel viewModel;
    private DrugListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_search_medication, container, false);
        viewModel = new ViewModelProvider(this).get(SearchMedicationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupRecyclerView();
        observeViewModel();

        binding.btnLogin.setOnClickListener(v -> viewModel.onSearchClicked());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBottomSheetState(view);
    }

    private void observeViewModel() {
        // Error Message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> Utility.alerter(getContext(), s));

        // Search Response
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), json -> {
            Log.v("JSON--->", json.toString());

            List<DrugItem> items = parseDrugItems(json);
            if (items.isEmpty()) {
                Utility.alerter(getContext(), "No results found");
            }

            adapter.setDrugList(items); // Show data in RecyclerView
        });
    }

    private void setBottomSheetState(View view) {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ((View) requireView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    private void setupRecyclerView() {
        adapter = new DrugListAdapter(getContext());
        binding.rvMedication.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMedication.setAdapter(adapter);
        //Swipe to delete functionality
        int bSize = Utility.getDimensionPixelSize(requireContext(), com.intuit.sdp.R.dimen._70sdp);
        int tSize = Utility.getDimensionPixelSize(requireContext(), com.intuit.sdp.R.dimen._12sdp);
        new SwipeHelper(requireActivity(), binding.rvMedication, bSize, tSize) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                try {
                    //Add your delete button
                    underlayButtons.add(new SwipeHelper.UnderlayButton(getString(R.string.delete), 0, requireActivity().getColor(R.color.del_color), pos -> {

                    }));
                } catch (Exception e) {
                    Log.e("An error occurred:", Objects.requireNonNull(e.getMessage()));
                }
            }
        };
    }


    private List<DrugItem> parseDrugItems(JsonObject jsonObject) {
        List<DrugItem> drugList = new ArrayList<>();

        if (jsonObject == null || !jsonObject.has("drugGroup")) return drugList;

        JsonObject drugGroup = jsonObject.getAsJsonObject("drugGroup");
        if (!drugGroup.has("conceptGroup")) return drugList;

        JsonArray conceptGroups = drugGroup.getAsJsonArray("conceptGroup");

        for (JsonElement groupElement : conceptGroups) {
            JsonObject groupObj = groupElement.getAsJsonObject();

            String tty = groupObj.has("tty") ? groupObj.get("tty").getAsString() : "";
            if (!tty.equals("SBD") && !tty.equals("SCD")) continue;

            if (!groupObj.has("conceptProperties")) continue;

            JsonArray conceptProps = groupObj.getAsJsonArray("conceptProperties");

            for (JsonElement propElement : conceptProps) {
                JsonObject propObj = propElement.getAsJsonObject();

                String name = propObj.has("name") ? getFirstDrugName(propObj.get("name").getAsString()) : "No Name";
                String synonym = propObj.has("synonym") ? propObj.get("synonym").getAsString() : "No Synonym";
                String rxcui = propObj.has("rxcui") ? propObj.get("rxcui").getAsString() : "-";

                DrugItem item = new DrugItem(name, synonym, rxcui);
                drugList.add(item);
            }
        }

        return drugList;
    }

    private static String getFirstDrugName(String fullName) {
        try {
            // Split by slash to get individual components
            String[] parts = fullName.split("/");
            if (parts.length > 0) {
                // Trim whitespace
                String firstPart = parts[0].trim();

                // Further split by space to get just the name
                String[] nameParts = firstPart.split(" ");
                return nameParts[0]; // e.g., "alanine"
            }
        } catch (Exception e) {
            Log.e("Exception", Objects.requireNonNull(e.getMessage()));
        }
        return fullName; // fallback
    }
}
