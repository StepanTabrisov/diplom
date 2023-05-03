package com.example.diplomproject;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(){
        InitRetrofit();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private void InitRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:9000")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
}
