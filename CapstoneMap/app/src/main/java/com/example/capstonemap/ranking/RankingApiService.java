package com.example.capstonemap.ranking;

import com.example.capstonemap.routes.RouteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RankingApiService {

    // @Body -> 객체를 보냄, @Query -> 값을 전달함, @Path -> url의 {} 부분에 값을 삽입함

    @GET("/api/{routeId}/rankings")
    Call<List<RankingDto>> getRankings(@Path("routeId") Long routeId);

    @GET("/api/{userId}/{routeId}/rankings")
    Call<RankingDto> getMyRanking(@Path("userId") Long userId, @Path("routeId") Long routeId);
}
