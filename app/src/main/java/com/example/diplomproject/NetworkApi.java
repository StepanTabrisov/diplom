package com.example.diplomproject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkApi {
    @POST("/users/find-user")
    Call<UserData> CheckUserData(@Body String login);

    @POST("/users/add")
    Call<Void> RegistrationUser(@Body UserData user);

    @GET("/users/get-all")
    Call<List<UserData>> getAllUsers();

    @Multipart
    @POST("/api/uploadfile")
    Call<ResponseBody> upload(@Part("description") RequestBody description, @Part MultipartBody.Part file);
}
