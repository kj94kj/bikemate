package com.example.capstonemap.bootApi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/greeting")
    Call<String> getGreeting();
}