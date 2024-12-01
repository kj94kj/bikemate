package com.example.capstonemap.rival;

import com.example.capstonemap.routes.RouteDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RivalApiService {

    @PUT("/api/users/{userId}/{otherId}/{routeId}")
    Call<Void> setRival(@Path("userId") Long userId, @Path("otherId") Long otherId, @Path("routeId") Long routeId);

    @GET("/api/users/{userId}/{routeId}")
    Call<RivalDto> getRival(@Path("userId") Long userId, @Path("routeId") Long routeId);

    @DELETE("/api/users/{userId}/{otherId}/{routeId}")
    Call<Void> deleteRival(@Path("userId") Long userId, @Path("otherId") Long otherId, @Path("routeId") Long routeId);
}

