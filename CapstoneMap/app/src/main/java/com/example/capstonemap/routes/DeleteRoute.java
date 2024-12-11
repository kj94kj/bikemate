package com.example.capstonemap.routes;

import android.view.View;

import com.example.capstonemap.databinding.ActivityMapsBinding;

public class DeleteRoute {
    static RouteRepository routeRepository=new RouteRepository();

    private static void deleteRoute(Long routeId, Long userId) {
        routeRepository.deleteRoute(routeId, userId,
                // 성공 시 routeDtoList에 데이터 저장
                () -> {
                    System.out.println("루트"+ routeId +"가 삭제되었습니다.");

                },
                // 실패 시 처리
                () -> {
                    System.out.println("루트 삭제에 실패했습니다.");
                }
        );
    }

    /*public static void deleteButton(ActivityMapsBinding binding, Long routeId, Long userId){
        binding.DeleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteRoute(routeId, userId);
                    }
                }
        );
    }*/
}
