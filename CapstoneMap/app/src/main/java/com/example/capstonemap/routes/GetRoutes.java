package com.example.capstonemap.routes;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.capstonemap.databinding.ActivityMapsBinding;
import com.example.capstonemap.polyLine.PolyLine;
import com.example.capstonemap.user.GetUserRoutes;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

//RouteApiService의 getAllRoutes를 쓰는 클래스
public class GetRoutes {
    static RouteRepository routeRepository=new RouteRepository();
    static List<RouteDto> routeDtoList= new ArrayList<>();

    // 이름은 getAllRoutes지만 사실 routeDtoList 필드에 저장하는(set) 역할도 함.
    // 루트를 다 가져오지말고 몇km 반경이내로만 가져오게 하는게 나을수도있음.
    // 현재 drawAllRoutes는 뺌.
    public static List<RouteDto> getAllRoutes() {
        routeRepository.getAllRoutes(
                // 성공 시 routeDtoList에 데이터 저장
                routes -> {
                    routeDtoList.clear();
                    routeDtoList.addAll(routes);
                    System.out.println("모든 루트가 routeDtoList에 저장되었습니다.");

                },
                // 실패 시 처리
                () -> {
                    System.out.println("루트 조회에 실패했습니다.");
                }
        );

        return routeDtoList;
    }

    // 이 함수를 가까운 루트 3개의 경우 폴리라인을 그리는 로직으로 바꾸면 될듯
    // Double[] location을 보면 아직 2개의 점만으로 진행함. 나중에 locationList가 3개를 넘어갈경우 문제가 생길거임.
    // routeDto는 폴리라인 단위임 즉 routeDtoList의 인덱스가 5이면 현재 5개의 폴리라인이 있다는거임.
    private static void drawAllRoutes(List<RouteDto> routeDtoList){
        for(RouteDto routeDto : routeDtoList){
            List<Double[]> locationList = routeDto.getLocationList();
            LatLng[] latlngs=new LatLng[locationList.size()];

            for (int i = 0; i < locationList.size(); i++) {
                Double[] location = locationList.get(i);
                latlngs[i] = new LatLng(location[0], location[1]);
            }

            PolyLine.getDirections(latlngs[0], latlngs[1], encodedPath -> {
                if (encodedPath == null || encodedPath.isEmpty()) {
                    Log.e("ENCODED_PATH_ERROR", "Encoded path is null or empty.");
                    return;
                }

                // 여기에서 필요한 작업 수행
                // 예를 들어, encodedPath를 사용해 경로를 디코딩하거나 저장
                List<LatLng> decodedPath = PolyUtil.decode(encodedPath);
                Log.d("DECODED_PATH", "Decoded path: " + decodedPath);

                // 만약 추가적인 작업이 있다면 여기에서 수행
            });
        }
    }

    public static void getAllRoutesButton(ActivityMapsBinding binding, List<RouteDto> routeDtoList){
        binding.GetAllRoutesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // GetAllRoutes의 getAllRoutes 메서드를 호출하여 루트 가져오기
                        List<RouteDto> newRoutes = getAllRoutes(); // 새로운 데이터를 가져옴
                        routeDtoList.clear(); // 기존 리스트를 비움
                        routeDtoList.addAll(newRoutes);

                        binding.GetUserRoutesButton.performClick();

                    }
                }
        );
    }

    public static void getUserIdRecordDtoListButton(ActivityMapsBinding binding, List<RouteDto> routeDtoList, Long userId){
        binding.GetUserRoutesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // GetAllRoutes의 getAllRoutes 메서드를 호출하여 루트 가져오기
                        List<RouteDto> newRoutes = getRoutesByRecordUserId(userId); // 새로운 데이터를 가져옴
                        routeDtoList.clear(); // 기존 리스트를 비움
                        routeDtoList.addAll(newRoutes);
                    }
                }
        );
    }

    public static RouteDto selectRoute(Long id){
        if(routeDtoList!=null){
            //아이디가 같은걸 찾음.
        }

        return null;
    }

    // 현재 minLength와 maxLength를 받아 그 범위안에있는 루트를 가져오게함
    public static List<RouteDto> getLengthRoutes(Double minLength, Double maxLength) {
        routeRepository.getLengthRoutes(
                minLength, maxLength,
                // 성공 시 routeDtoList에 데이터 저장
                routes -> {
                    routeDtoList.clear();
                    routeDtoList.addAll(routes);
                    System.out.println("모든 루트가 routeDtoList에 저장되었습니다.");

                    drawAllRoutes(routeDtoList);
                },
                // 실패 시 처리
                () -> {
                    System.out.println("루트 조회에 실패했습니다.");
                }
        );

        return routeDtoList;
    }

    public static List<RouteDto> getRoutesByRecordUserId(Long userId) {
        routeRepository.getRoutesByRecordUserId(
                userId,
                // 성공 시 routeDtoList에 데이터 저장
                routes -> {
                    routeDtoList.clear();
                    routeDtoList.addAll(routes);
                    System.out.println("모든 루트가 routeDtoList에 저장되었습니다.");

                    drawAllRoutes(routeDtoList);
                },
                // 실패 시 처리
                () -> {
                    System.out.println("루트 조회에 실패했습니다.");
                }
        );

        return routeDtoList;
    }
}
