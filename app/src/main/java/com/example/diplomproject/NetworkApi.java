package com.example.diplomproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetworkApi {
    @POST("/users/find-user")
    Call<UserData> CheckUserData(@Body String login);

    @POST("/users/add")
    Call<Void> RegistrationUser(@Body UserData user);

    @GET("/users/get-all")
    Call<List<UserData>> getAllUsers();
}
