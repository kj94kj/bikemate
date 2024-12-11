package com.example.capstonemap.user;

import com.example.capstonemap.routes.RouteDto;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("/api/users")
    Call<Void> saveLogin(@Body UserDto userDto);

    @POST("/api/login")
    Call<Map<String, Object>> login(@Body UserDto userDto);
}
