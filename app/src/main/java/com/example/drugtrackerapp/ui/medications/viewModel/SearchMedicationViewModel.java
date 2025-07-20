package com.example.drugtrackerapp.ui.medications.viewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.drugtrackerapp.ui.medications.repository.SearchMedicationRepository;
import com.google.gson.JsonObject;

/**
 * ViewModel responsible for handling medication search functionality.
 * Manages the search query input, loading state, search results, and error messages.
 * Acts as a communication layer between the UI (SearchMedicationBottomSheetFragment) and data layer (SearchMedicationRepository).
 */
public class SearchMedicationViewModel extends ViewModel {
    /**
     * Two-way binding LiveData for the search query input field.
     * Initialized with an empty string.
     */
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    
    /**
     * LiveData to track the loading state during API calls.
     * Used to show/hide loading indicators in the UI.
     */
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    
    /**
     * Repository instance to handle API calls for medication searches.
     */
    private final SearchMedicationRepository repository;
    
    /**
     * Observers for repository LiveData to prevent memory leaks.
     */
    private final Observer<JsonObject> successObserver;
    private final Observer<String> errorObserver;

    /**
     * LiveData to store and expose the search results from the API.
     * Contains the raw JsonObject response from the medication search API.
     */
    private final MutableLiveData<JsonObject> searchResults = new MutableLiveData<>();
    
    /**
     * LiveData to store and expose error messages that occur during API calls.
     */
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /**
     * Constructor initializes the repository and sets up observers for repository LiveData.
     * These observers forward repository events to the ViewModel's LiveData objects.
     */
    public SearchMedicationViewModel() {
        repository = new SearchMedicationRepository();
        
        // Create observers and store references to them for later cleanup
        successObserver = searchResults::postValue;
        errorObserver = errorMessage::postValue;
        
        // Observe repository success response and forward to searchResults LiveData
        repository.liveDataSuccessResponse.observeForever(successObserver);

        // Observe repository error response and forward to errorMessage LiveData
        repository.liveDataIsFailedResponse.observeForever(errorObserver);
    }
    
    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * This is the place to clean up any resources, including removing observers to prevent memory leaks.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        
        // Remove observers to prevent memory leaks
        if (successObserver != null) {
            repository.liveDataSuccessResponse.removeObserver(successObserver);
        }
        
        if (errorObserver != null) {
            repository.liveDataIsFailedResponse.removeObserver(errorObserver);
        }
    }

    /**
     * Provides the loading state as LiveData to the UI.
     * @return LiveData<Boolean> indicating whether a loading operation is in progress
     */
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Handles the search button click event.
     * Validates the search query and initiates the API call if valid.
     * Sets loading state and handles empty query cases.
     */
    public void onSearchClicked() {
        // Set loading state to true to show loading indicator
        loading.setValue(true);
        
        // Get the current search query value
        String query = searchQuery.getValue();
        
        // Validate query is not null or empty
        if (query != null && !query.trim().isEmpty()) {
            // Call repository to execute the API call
            repository.callGetSearchedDrugsApi(query);
        } else {
            // If query is empty, post an empty result and don't make API call
            searchResults.postValue(new JsonObject());
        }
    }

    /**
     * Provides the search results as LiveData to the UI.
     * @return LiveData<JsonObject> containing the API response with drug search results
     */
    public LiveData<JsonObject> getSearchResults() {
        return searchResults;
    }

    /**
     * Provides error messages as LiveData to the UI.
     * @return LiveData<String> containing error messages from failed API calls
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
