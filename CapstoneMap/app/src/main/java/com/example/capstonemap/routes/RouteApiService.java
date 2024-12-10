package com.example.capstonemap.routes;

import com.example.capstonemap.locationUpdate.UserRecordDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RouteApiService {
    // @Body -> 객체를 보냄, @Query -> 값을 전달함, @Path -> url의 {} 부분에 값을 삽입함
    @POST("/api/users/{userId}/routes")
    Call<Void> saveRoute(@Body RouteDto routeDto, @Path("userId") Long userId);

    @GET("/api/routes")
    Call<List<RouteDto>> getAllRoutes();

    // 만들었거나 즐겨찾기 한 루트를 볼 수 있음
    // 여기서 유저가 선택한 루트 정보도 볼 수 있어야하고, 루트의 encodedString도 따로 저장해야함.
    @GET("/api/users/{userId}/routes")
    Call<List<RouteDto>> getUserRoutes(@Path("userId") Long userId);

    // 루트를 삭제함, 루트 id와 유저 id를 매개변수로 받고 유저 id와 루트 id가 같다면 자기가 만든 루트니까
    // 유저 루트 목록과 루트 목록에서 삭제, 같지 않다면 다른 사람이 만든 루트니까 유저 루트 목록에서만 삭제
    // 삭제할 루트를 고르는 기능도 구현 해야함
    @DELETE("/api/users/{userId}/routes")
    Call<Void> deleteRoute( @Path("userId") Long userId, @Query("routeId") Long routeId);

    @GET("/api/users/length/routes/{minLength}/{maxLength}")
    Call<List<RouteDto>> getLengthRoutes(@Path("minLength") Double minLength, @Path("maxLength") Double maxLength);

    @GET("/api/users/{userId}/records")
    Call<List<RouteDto>> getRoutesByRecordUserId(@Path("userId") Long userId);
}
    