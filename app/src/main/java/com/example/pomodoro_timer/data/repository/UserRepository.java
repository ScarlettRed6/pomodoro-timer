package com.example.pomodoro_timer.data.repository;

import com.example.pomodoro_timer.data.network.RetrofitClient;
import com.example.pomodoro_timer.data.network.api.UserApi;
import com.example.pomodoro_timer.data.network.dto.User;

import retrofit2.Call;

public class UserRepository {
    private final UserApi userApi;

    public UserRepository() {
        userApi = RetrofitClient.getClient().create(UserApi.class);
    }

    public Call<User> login(String email, String password) {
        User user = new User(email, password);
        return userApi.login(user);
    }

    public Call<User> register(String email, String password) {
        User user = new User(email, password);
        return userApi.register(user);
    }

    public Call<User> getUserById(int id) {
        return userApi.getUser(id);
    }
}
