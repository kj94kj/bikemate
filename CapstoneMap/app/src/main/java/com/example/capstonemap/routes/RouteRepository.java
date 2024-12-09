package com.example.capstonemap.routes;

import androidx.core.util.Consumer;

import com.example.capstonemap.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteRepository {
    // 여기다가 이름도 적어서 보내야함.
    public void saveRoute(RouteDto routeDto, Long userId, Runnable onSuccess, Runnable onError) {
        ApiClient.getRouteApiService().saveRoute(routeDto, userId).enqueue(new Callback<Void>() {
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

    public void getAllRoutes(Consumer<List<RouteDto>> onSuccess, Runnable onError) {
        ApiClient.getRouteApiService().getAllRoutes().enqueue(new Callback<List<RouteDto>>() {
            @Override
            public void onResponse(Call<List<RouteDto>> call, Response<List<RouteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("모든 루트 조회 성공");
                } else {
                    onError.run();
                    System.out.println("모든 루트 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RouteDto>> call, Throwable t) {
                onError.run();
                System.out.println("모든 루트 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void getUserRoutes(Long userId ,Consumer<List<RouteDto>> onSuccess, Runnable onError) {
        ApiClient.getRouteApiService().getUserRoutes(userId).enqueue(new Callback<List<RouteDto>>() {
            @Override
            public void onResponse(Call<List<RouteDto>> call, Response<List<RouteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("유저 루트 조회 성공");
                } else {
                    onError.run();
                    System.out.println("유저 루트 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RouteDto>> call, Throwable t) {
                onError.run();
                System.out.println("유저 루트 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void deleteRoute(Long routeId, Long userId, Runnable onSuccess, Runnable onError) {
        ApiClient.getRouteApiService().deleteRoute(routeId, userId).enqueue(new Callback<Void>() {
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

    public void getLengthRoutes(Double minLength, Double maxLength, Consumer<List<RouteDto>> onSuccess, Runnable onError) {
        ApiClient.getRouteApiService().getLengthRoutes(minLength, maxLength).enqueue(new Callback<List<RouteDto>>() {
            @Override
            public void onResponse(Call<List<RouteDto>> call, Response<List<RouteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("유저 루트 조회 성공");
                } else {
                    onError.run();
                    System.out.println("유저 루트 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RouteDto>> call, Throwable t) {
                onError.run();
                System.out.println("유저 루트 조회 실패, 오류: " + t.getMessage());
            }
        });
    }
}
