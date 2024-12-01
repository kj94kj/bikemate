package com.example.capstonemap.rival;

import androidx.core.util.Consumer;

import com.example.capstonemap.ApiClient;
import com.example.capstonemap.ranking.RankingDto;
import com.example.capstonemap.routes.RouteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RivalRepository {
    public void getRival(Long userId, Long routeId , Consumer<RivalDto> onSuccess, Runnable onError) {
        ApiClient.getRivalApiService().getRival(userId, routeId).enqueue(new Callback<RivalDto>() {
            @Override
            public void onResponse(Call<RivalDto> call, Response<RivalDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("랭킹 조회 성공");
                } else {
                    onError.run();
                    System.out.println("랭킹 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RivalDto> call, Throwable t) {
                onError.run();
                System.out.println("랭킹 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void setRival(Long userId, Long otherId, Long routeId, Runnable onSuccess, Runnable onError) {
        ApiClient.getRivalApiService().setRival(userId, otherId, routeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                    System.out.println("API 호출 성공");
                } else {
                    onError.run();
                    System.out.println("API 호출 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onError.run();
                System.out.println("API 호출 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void deleteRival(Long userId, Long otherId, Long routeId, Runnable onSuccess, Runnable onError) {
        ApiClient.getRivalApiService().deleteRival(userId, otherId, routeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                    System.out.println("API 호출 성공");
                } else {
                    onError.run();
                    System.out.println("API 호출 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onError.run();
                System.out.println("API 호출 실패, 오류: " + t.getMessage());
            }
        });
    }
}
