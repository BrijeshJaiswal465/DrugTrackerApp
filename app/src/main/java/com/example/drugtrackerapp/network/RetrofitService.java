package com.example.drugtrackerapp.network;

import android.content.Context;
import android.util.Log;

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

public class RetrofitService {

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES).addInterceptor(logging).addNetworkInterceptor(new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder().method(original.method(), original.body()).build();


            Response response = chain.proceed(request);
//
//            assert response.body() != null;
//            Log.v("RESPONSE-->", response.body().string());
//            //Authorization checking
//            if (response.code() == 401) {
//
//                return response;
//            }
            return response;
        }
    });

    private static final Gson gson = new GsonBuilder().setLenient().create();

    private static final Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://rxnav.nlm.nih.gov/REST/");

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit.create(serviceClass);
    }

}
