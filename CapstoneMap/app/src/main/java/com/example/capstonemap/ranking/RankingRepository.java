package com.example.capstonemap.ranking;

import androidx.core.util.Consumer;

import com.example.capstonemap.ApiClient;
import com.example.capstonemap.routes.RouteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingRepository {
    public void getRankings(Long routeId , Consumer<List<RankingDto>> onSuccess, Runnable onError) {
        ApiClient.getRankingApiService().getRankings(routeId).enqueue(new Callback<List<RankingDto>>() {
            @Override
            public void onResponse(Call<List<RankingDto>> call, Response<List<RankingDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("랭킹 조회 성공");
                } else {
                    onError.run();
                    System.out.println("랭킹 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RankingDto>> call, Throwable t) {
                onError.run();
                System.out.println("랭킹 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void getMyRanking(Long userId, Long routeId , Consumer<RankingDto> onSuccess, Runnable onError) {
        ApiClient.getRankingApiService().getMyRanking(userId, routeId).enqueue(new Callback<RankingDto>() {
            @Override
            public void onResponse(Call<RankingDto> call, Response<RankingDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("랭킹 조회 성공");
                } else {
                    onError.run();
                    System.out.println("랭킹 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RankingDto> call, Throwable t) {
                onError.run();
                System.out.println("랭킹 조회 실패, 오류: " + t.getMessage());
            }
        });
    }
}
