package com.example.capstonemap.user;

import android.view.View;

import com.example.capstonemap.databinding.ActivityMapsBinding;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.routes.RouteRepository;

import java.util.ArrayList;
import java.util.List;

public class GetUserRoutes {
    static RouteRepository routeRepository=new RouteRepository();
    static List<RouteDto> userRouteDtoList = new ArrayList<>(); ;


    private static List<RouteDto> getUserRoutes(Long userId) {
        routeRepository.getUserRoutes(
                userId,
                // 성공 시 routeDtoList에 데이터 저장
                routes -> {
                    userRouteDtoList.clear();
                    userRouteDtoList.addAll(routes);
                    System.out.println("모든 루트가 userRouteDtoList에 저장되었습니다.");

                },
                // 실패 시 처리
                () -> {
                    System.out.println("루트 조회에 실패했습니다.");
                }
        );

        return userRouteDtoList;
    }

    /*public static void getUserRoutesButton(ActivityMapsBinding binding, Long userId){
        binding.GetUserRoutesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUserRoutes(userId);
                        getUserRouteList(binding);
                    }
                }
        );
    }*/

    public static void getUserRouteList(ActivityMapsBinding binding){
        StringBuilder routesText = new StringBuilder();
        for (RouteDto route : userRouteDtoList) {
            routesText.append(route.toString()).append("\n");
        }
        binding.routeTextView.setText(routesText.toString());
    }

    // 인덱스로 루트 선택함.
    public static RouteDto selectRoute(int index){
        return userRouteDtoList.get(index);
    }
}
