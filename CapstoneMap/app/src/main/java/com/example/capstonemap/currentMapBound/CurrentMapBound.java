package com.example.capstonemap.currentMapBound;

import android.util.Log;

import com.example.capstonemap.routes.RouteDto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

//현재 화면의 좌표 경계들을 가져오고, 그 범위내에 시작점이 있는 루트들을 보여줌
//그 루트를 선택했을때는 화면 범위 밖에 가도 보이는등의 추가 작업필요
public class CurrentMapBound {

    public static LatLngBounds getCurrentMapBounds(GoogleMap googleMap) {
        return googleMap.getProjection().getVisibleRegion().latLngBounds;
    }

    public static List<RouteDto> getBoundRouteDtoList(List<RouteDto> routeDtoList, LatLngBounds bounds) {
        List<RouteDto> boundRouteList = new ArrayList<>();

        for (RouteDto route : routeDtoList) {

            LatLng startPoint = new LatLng(route.getStartLocation()[0], route.getStartLocation()[1]); // 루트의 시작점
            if (bounds.contains(startPoint)) {
                boundRouteList.add(route);
            }
        }

        return boundRouteList;
    }

    // 나중에 모든 루트 받고 -> 그걸 allRoutes에 넣고 -> 콜백함수 정의해야함
    // 호출간격 문제는 나중에 해결하기
    public static void cameraBoundListener(GoogleMap googleMap, List<RouteDto> allRoutes,
                                           OnBoundListener listener) {


        googleMap.setOnCameraIdleListener(() -> {
            // 현재 화면 범위 계산
            Log.d("CurrentMapBound", "Camera idle detected");
            LatLngBounds bounds = getCurrentMapBounds(googleMap);

            // 화면 범위 내의 루트 필터링
            List<RouteDto> boundRouteDtoList = getBoundRouteDtoList(allRoutes, bounds);

            Log.d("CurrentMapBound", "Bounded routes: " + boundRouteDtoList.size());

            listener.onBound(boundRouteDtoList);
        });
    }
}
