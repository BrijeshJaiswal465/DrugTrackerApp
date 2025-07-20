package com.example.drugtrackerapp.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface defining the API endpoints for the application.
 * This interface is used by Retrofit to generate an implementation that makes
 * network requests to the RxNav API service.
 */
public interface ApiInterface {
    /**
     * Endpoint to search for drugs by name.
     * Makes a GET request to the "drugs.json" endpoint of the RxNav API.
     * 
     * @param drugName The name of the drug to search for
     * @param expandValue The expand parameter value (typically "psn" to include preferred synonym names)
     * @return A Call object containing a JsonObject with the API response
     */
    @GET("drugs.json")
    Call<JsonObject> callGetSearchedDrugsApi(
            @Query("name") String drugName,
            @Query("expand") String expandValue
    );
}
