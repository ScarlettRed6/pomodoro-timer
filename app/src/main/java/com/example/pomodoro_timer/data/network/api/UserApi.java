package com.example.pomodoro_timer.data.network.api;

import com.example.pomodoro_timer.data.network.dto.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @POST("/api/users/register")
    Call<User> register(@Body User user);

    @POST("/api/users/login")
    Call<User> login(@Body User user);

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") int id);

}
