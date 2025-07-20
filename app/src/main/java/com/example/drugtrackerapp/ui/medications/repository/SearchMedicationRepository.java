package com.example.drugtrackerapp.ui.medications.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.drugtrackerapp.network.ApiInterface;
import com.example.drugtrackerapp.network.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class that handles API calls for medication search functionality.
 * Acts as an abstraction layer between the ViewModel and the Retrofit service.
 * This class is responsible for making network requests to search for medications
 * and exposing the results through LiveData objects.
 */
public class SearchMedicationRepository {

    /**
     * Retrofit interface for making API calls.
     */
    private final ApiInterface apiInterface;
    
    /**
     * LiveData to expose successful API responses to observers.
     * Contains the JSON response from the medication search API.
     */
    public MutableLiveData<JsonObject> liveDataSuccessResponse = new MutableLiveData<>();
    
    /**
     * LiveData to expose error messages to observers when API calls fail.
     */
    public MutableLiveData<String> liveDataIsFailedResponse = new MutableLiveData<>();

    /**
     * Constructor that initializes the Retrofit API interface.
     * Uses the RetrofitService to create an implementation of the ApiInterface.
     */
    public SearchMedicationRepository() {
        apiInterface = RetrofitService.createService(ApiInterface.class);
    }

    /**
     * Makes an asynchronous API call to search for medications by name.
     * Uses Retrofit's enqueue method to perform the request on a background thread.
     * Results are posted to the appropriate LiveData objects based on success or failure.
     * 
     * @param drugName The name of the drug to search for
     */
    public void callGetSearchedDrugsApi(String drugName) {
        apiInterface.callGetSearchedDrugsApi(drugName, "psn").enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Post successful response to the success LiveData
                    liveDataSuccessResponse.postValue(response.body());
                } else {
                    // Post error message to the failure LiveData
                    liveDataIsFailedResponse.postValue("API Error: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // Post network or other failure message to the failure LiveData
                liveDataIsFailedResponse.postValue("Failure: " + t.getMessage());
            }
        });
    }
}
