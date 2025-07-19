package com.example.drugtrackerapp.ui.medications.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.drugtrackerapp.network.ApiInterface;
import com.example.drugtrackerapp.network.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMedicationRepository {

    private final ApiInterface apiInterface;
    public MutableLiveData<JsonObject> liveDataSuccessResponse = new MutableLiveData<>();
    public MutableLiveData<String> liveDataIsFailedResponse = new MutableLiveData<>();

    public SearchMedicationRepository() {
        apiInterface = RetrofitService.createService(ApiInterface.class);
    }

    public void callGetSearchedDrugsApi(String drugName) {
        apiInterface.callGetSearchedDrugsApi(drugName, "psn").enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveDataSuccessResponse.postValue(response.body());
                } else {
                    liveDataIsFailedResponse.postValue("API Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                liveDataIsFailedResponse.postValue("Failure: " + t.getMessage());
            }
        });
    }
}
