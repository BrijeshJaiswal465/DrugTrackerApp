package com.example.drugtrackerapp.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("drugs.json")
    Call<JsonObject> callGetSearchedDrugsApi(
            @Query("name") String drugName,
            @Query("expand") String expandValue
    );
}
