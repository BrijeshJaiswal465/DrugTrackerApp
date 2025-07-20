package com.example.drugtrackerapp.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton service class that provides configured Retrofit instances for making API calls.
 * This class handles the setup of OkHttpClient with appropriate timeouts, logging,
 * and network interceptors for all API communications in the application.
 * The service is configured to connect to the RxNav API from the National Library of Medicine,
 * which provides drug information services.
 */
public class RetrofitService {

    /**
     * HTTP logging interceptor configured to log basic request and response information.
     * Useful for debugging API calls during development.
     */
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    
    /**
     * Configured OkHttpClient.Builder with extended timeouts (10 minutes) for all operations
     * and network interceptors for request/response handling.
     * The long timeout values accommodate potentially slow network conditions or large responses.
     */
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .addNetworkInterceptor(new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder().method(original.method(), original.body()).build();
            return chain.proceed(request);
        }
    });

    /**
     * Gson instance configured with lenient parsing to handle potential JSON format issues.
     * This helps prevent parsing failures when the API returns slightly malformed JSON.
     */
    private static final Gson gson = new GsonBuilder().setLenient().create();

    /**
     * Retrofit.Builder instance configured with the RxNav API base URL.
     * RxNav is a service from the National Library of Medicine that provides
     * drug information, including drug names, RxCUI identifiers, and related data.
     */
    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://rxnav.nlm.nih.gov/REST/");

    /**
     * Creates and returns a Retrofit service implementation for the specified service interface.
     * This method builds the complete Retrofit instance with the configured OkHttpClient,
     * Gson converter, and base URL, then generates an implementation of the service interface.
     *
     * @param <S> The type of the service interface
     * @param serviceClass The class object of the service interface
     * @return An implementation of the service interface that can be used to make API calls
     */
    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(serviceClass);
    }

}
