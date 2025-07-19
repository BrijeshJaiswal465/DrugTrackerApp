package com.example.drugtrackerapp.ui.medications.viewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.drugtrackerapp.ui.medications.repository.SearchMedicationRepository;
import com.google.gson.JsonObject;

public class SearchMedicationViewModel extends ViewModel {
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    private final SearchMedicationRepository repository;

    private final MutableLiveData<JsonObject> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchMedicationViewModel() {
        repository = new SearchMedicationRepository();
        // Observe repository LiveData and pass to ViewModel LiveData
        repository.liveDataSuccessResponse.observeForever(searchResults::postValue);

        repository.liveDataIsFailedResponse.observeForever(errorMessage::postValue);
    }

    public void onSearchClicked() {
        String query = searchQuery.getValue();
        if (query != null && !query.trim().isEmpty()) {
            repository.callGetSearchedDrugsApi(query);
        }
    }

    public LiveData<JsonObject> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
