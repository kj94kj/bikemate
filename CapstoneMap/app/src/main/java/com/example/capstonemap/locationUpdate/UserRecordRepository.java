package com.example.capstonemap.locationUpdate;

import androidx.core.util.Consumer;

import com.example.capstonemap.ApiClient;
import com.example.capstonemap.routes.RouteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRecordRepository {

    // UserUpdateDto를 저장하는 함수
    public static void saveMyRecord(UserRecordDto userRecordDto, Long userId, Long routeId, Runnable onSuccess, Runnable onError) {
        ApiClient.getUserRecordApiService().saveMyRecord(userRecordDto, userId, routeId).enqueue(new Callback<Void>() {
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

    public void getMyOldRecord(Long userId, Long routeId, Consumer<UserRecordDto> onSuccess, Runnable onError) {
        ApiClient.getUserRecordApiService().getMyOldRecord(userId, routeId).enqueue(new Callback<UserRecordDto>() {
            @Override
            public void onResponse(Call<UserRecordDto> call, Response<UserRecordDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("유저 기록 조회 성공");
                } else {
                    onError.run();
                    System.out.println("유저 기록 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserRecordDto> call, Throwable t) {
                onError.run();
                System.out.println("유저 기록 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    public void getOldRecord(Long userId, Long routeId, Consumer<UserRecordDto> onSuccess, Runnable onError) {
        ApiClient.getUserRecordApiService().getOldRecord(userId, routeId).enqueue(new Callback<UserRecordDto>() {
            @Override
            public void onResponse(Call<UserRecordDto> call, Response<UserRecordDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("유저 기록 조회 성공");
                } else {
                    onError.run();
                    System.out.println("유저 기록 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserRecordDto> call, Throwable t) {
                onError.run();
                System.out.println("유저 기록 조회 실패, 오류: " + t.getMessage());
            }
        });
    }

    //Racing에서 씀
    public void getTop5Record(Long userId, Long routeId, Consumer<List<UserRecordDto>> onSuccess, Runnable onError) {
        ApiClient.getUserRecordApiService().getTop5Record(userId, routeId).enqueue(new Callback<List<UserRecordDto>>() {
            @Override
            public void onResponse(Call<List<UserRecordDto>> call, Response<List<UserRecordDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body());
                    System.out.println("유저 루트 조회 성공");
                } else {
                    onError.run();
                    System.out.println("유저 루트 조회 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserRecordDto>> call, Throwable t) {
                onError.run();
                System.out.println("유저 루트 조회 실패, 오류: " + t.getMessage());
            }
        });
    }
}
