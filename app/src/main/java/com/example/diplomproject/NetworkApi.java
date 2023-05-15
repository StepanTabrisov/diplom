package com.example.diplomproject;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface NetworkApi {

    @POST("/users/find-user")
    Call<UserData> CheckUserData(@Body String login);

    @POST("/users/add")
    Call<Void> RegistrationUser(@Body UserData user);

    @GET("/users/get-all")
    Call<List<UserData>> getAllUsers();

    // Финальный вариант регистрации / авторизации
    @POST("/users/check_user")
    Call<Boolean> RegUser(@Body UserData user);

    @POST("/users/check_user_password")
    Call<Boolean> AuthUser(@Body UserData user);

    //загрузка списка
    @POST("/api/load/{username}")
    Call<Fields> GetDataList(@Path("username") String username, @Body String data);

    //Отправка списка
    @POST("/api/save/{username}")
    Call<ResponseBody> SaveDataList(@Path("username") String username, @Body Fields data);

    // загрузка файлов на сервер
    @Multipart
    @POST("/api/uploadfile")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    // загрузка файла на сервер (финальный вариант)
    @Multipart
    @POST("/api/upload/{username}")
    Call<ResponseBody> uploadFile(@Path("username") String username, @Part MultipartBody.Part file);

    // скачивание файлов с сервера
    @Streaming
    @POST("/api/download/{username}")
    Call<ResponseBody> downloadFile(@Path("username") String username, @Body String data);


}
