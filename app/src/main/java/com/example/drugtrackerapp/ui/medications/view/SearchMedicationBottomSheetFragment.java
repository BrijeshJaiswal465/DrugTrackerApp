package com.example.drugtrackerapp.ui.medications.view;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.base.BottomSheetBaseFragment;
import com.example.drugtrackerapp.databinding.FragmentSearchMedicationBinding;
import com.example.drugtrackerapp.ui.medications.model.DrugItem;
import com.example.drugtrackerapp.ui.medications.view.adapter.DrugListAdapter;
import com.example.drugtrackerapp.ui.medications.viewModel.SearchMedicationViewModel;
import com.example.drugtrackerapp.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A bottom sheet fragment that allows users to search for medications using an API.
 * This fragment displays a search input field, search button, and a list of search results.
 * When a medication is selected from the results, it opens the DrugDetailsBottomSheetFragment
 * to display more information about the selected medication.
 */
public class SearchMedicationBottomSheetFragment extends BottomSheetBaseFragment implements DrugListAdapter.onItemClickListener {
    /**
     * Data binding instance for accessing the fragment's views.
     */
    private FragmentSearchMedicationBinding binding;

    /**
     * ViewModel instance that handles the search functionality and data.
     */
    private SearchMedicationViewModel viewModel;

    /**
     * Adapter for the RecyclerView that displays the search results.
     */
    private DrugListAdapter adapter;

    /**
     * Called when the fragment is first created.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates and configures the view for this fragment.
     * Sets up data binding, initializes the ViewModel, configures the RecyclerView,
     * and sets up click listeners for the search button and back button.
     *
     * @param inflater           The LayoutInflater object to inflate views
     * @param container          The parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_search_medication, container, false);
        viewModel = new ViewModelProvider(this).get(SearchMedicationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupRecyclerView();
        observeViewModel();
        //Set text
        binding.includeBottomSheetHeader.tvTitle.setText(R.string.search_medication);

        binding.btnSearch.setOnClickListener(v -> viewModel.onSearchClicked());
        binding.includeBottomSheetHeader.llBack.setOnClickListener(v -> dismiss());

        View view = binding.getRoot();
        //Visibility search button
        Utility.manageVisibilityWhenKeyboardOpen(view, binding.btnSearch);

        return view;
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored
     * in to the view. Sets the bottom sheet to expanded state and configures the soft input mode
     * to adjust resize for proper keyboard handling.
     *
     * @param view               The View returned by onCreateView()
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBottomSheetState(view);
        Objects.requireNonNull(requireDialog().getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * Sets up observers for the ViewModel's LiveData objects.
     * Observes error messages, loading state, and search results to update the UI accordingly.
     */
    private void observeViewModel() {
        // Error Message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> {
            hideLoader();
            Utility.toast(getContext(), s);
        });

        // Observe loading state (e.g., show/hide progress bar)
        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    showLoader();
                }
            }
        });

        // Search Response
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), json -> {
            hideLoader();
            List<DrugItem> items = parseDrugItems(json);
            if (items.isEmpty()) {
                Utility.toast(getContext(), "No results found");
            }
            adapter.setDrugList(items);
        });
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

    /**
     * Initializes the RecyclerView with a LinearLayoutManager and sets up the adapter
     * for displaying drug search results.
     */
    private void setupRecyclerView() {
        adapter = new DrugListAdapter(getContext(), false, this);
        binding.rvMedication.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMedication.setAdapter(adapter);
    }


    /**
     * Parses the JSON response from the medication search API into a list of DrugItem objects.
     * Extracts relevant information such as name, synonym, and rxcui from the nested JSON structure.
     * Only includes items with specific TTY values (SBD or SCD).
     *
     * @param jsonObject The JSON response from the medication search API
     * @return A list of DrugItem objects parsed from the JSON response
     */
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

                String name = propObj.has("psn") ? propObj.get("psn").getAsString() : "";
                String synonym = propObj.has("synonym") ? propObj.get("synonym").getAsString() : "No Synonym";
                String rxcui = propObj.has("rxcui") ? propObj.get("rxcui").getAsString() : "-";

                if (!name.isEmpty()) {
                    DrugItem item = new DrugItem(name, synonym, rxcui);
                    drugList.add(item);
                }
            }
        }
        return drugList;
    }

    /**
     * Handles click events on items in the drug search results list.
     * Opens the DrugDetailsBottomSheetFragment to display detailed information about the selected drug.
     *
     * @param drugItem The DrugItem that was clicked in the list
     */
    @Override
    public void onItemClick(DrugItem drugItem) {
        //Show drug details
        DrugDetailsBottomSheetFragment fragment = new DrugDetailsBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DrugItem", drugItem);
        fragment.setArguments(bundle);
        fragment.show(getParentFragmentManager(), fragment.getTag());
    }
}
