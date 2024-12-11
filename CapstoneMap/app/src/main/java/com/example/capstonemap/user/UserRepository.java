package com.example.capstonemap.user;

import android.util.Log;

import com.example.capstonemap.ApiClient;
import com.example.capstonemap.routes.RouteDto;

import androidx.core.util.Consumer;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public void saveLogin(UserDto userDto, Runnable onSuccess, Runnable onError) {
        ApiClient.getUserApiService().saveLogin(userDto).enqueue(new Callback<Void>() {
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

    public void login(UserDto userDto, Consumer<Long> onSuccess, Runnable onError) {
        ApiClient.getUserApiService().login(userDto).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> responseBody = response.body();
                    if ("로그인 성공".equals(responseBody.get("message"))) {
                        Long userId = ((Number) responseBody.get("id")).longValue();
                        onSuccess.accept(userId);
                    } else {
                        onError.run();
                    }
                } else {
                    onError.run();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                onError.run();
            }
        });
    }
}
