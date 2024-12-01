package com.example.capstonemap.rival;

import com.example.capstonemap.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetRival {
    public static void setRival(Long userId, Long otherId, Long routeId, Runnable onSuccess, Runnable onFailure) {
        ApiClient.getRivalApiService().setRival(userId, otherId, routeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                    System.out.println("라이벌 설정 성공!");
                } else {
                    onFailure.run();
                    System.out.println("라이벌 설정 실패, 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onFailure.run();
                System.out.println("라이벌 설정 실패, 오류: " + t.getMessage());
            }
        });
    }
}
