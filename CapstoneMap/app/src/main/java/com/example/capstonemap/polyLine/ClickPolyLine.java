package com.example.capstonemap.polyLine;

import static com.example.capstonemap.distance.DistancePolyLine.calculateTotalLength;

import android.util.Log;

import com.example.capstonemap.distance.DistancePolyLine;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.routes.RouteRepository;
import com.example.capstonemap.user.UserDto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ClickPolyLine {
    static RouteRepository routeRepository=new RouteRepository();

    public static void clickPolyLine(GoogleMap mMap, UserDto userDto) {
        final LatLng[] origin = new LatLng[1];  // 출발지를 저장할 배열
        final LatLng[] destination = new LatLng[1];  // 도착지를 저장할 배열
        final RouteDto[] routeDto = new RouteDto[1];
        Double[] startLocation=new Double[2];
        ArrayList<Double[]> locationList=new ArrayList<>();


        // 지도를 길게 눌렀을 때 마커를 추가하고 폴리라인을 그리는 기능 설정
        mMap.setOnMapLongClickListener(latLng -> {
            if (origin[0] == null) {
                // 첫 번째 클릭 시: 출발지 설정
                origin[0] = latLng;

                // 지금 꾹 누른 출발지가 어디인지 나타나게.

            } else if (destination[0] == null) {
                // 두 번째 클릭 시: 도착지 설정
                destination[0] = latLng;

                // 출발지와 도착지를 이용하여 폴리라인 그리기
                PolyLine.getDirections(origin[0], destination[0], encodedPath -> {
                    if (encodedPath == null || encodedPath.isEmpty()) {
                        Log.e("ENCODED_PATH_ERROR", "Encoded path is null or empty.");
                        return;
                    }

                    locationList.add(new Double[]{origin[0].latitude, origin[0].longitude});
                    locationList.add(new Double[]{destination[0].latitude, destination[0].longitude});

                    startLocation[0] = origin[0].latitude;
                    startLocation[1] = origin[0].longitude;


                    PolyLine.drawPolylineOnMap(PolyLine.decodePolyline(encodedPath));

                    Double length = DistancePolyLine.calculateTotalLength(DistancePolyLine.encodedToLatLng(encodedPath));

                    routeDto[0] = new RouteDto("temporal", encodedPath, locationList, startLocation, length);

                    Gson gson = new Gson();
                    System.out.println(gson.toJson(routeDto[0]));

                    saveRoute(routeDto, userDto);
                });

            } else {
                // 기존 마커와 폴리라인 초기화 후, 새로운 출발지 설정
                PolyLine.removePolyline(PolyLine.getLastEncoded());
                locationList.clear();
                origin[0] = latLng;
                destination[0] = null;  // 새로운 출발지가 설정되었으므로 도착지는 null로 초기화
            }
        });
    }

    private static void saveRoute(final RouteDto[] routeDto, UserDto userDto){
        Long userId= userDto.getId();
        System.out.println("User ID: " + userId);


        //리파지토리, 스프링과 연동해서 경로를 만들면 저장함
        routeRepository.saveRoute(routeDto[0], userId,
                () -> System.out.println("Route saved successfully!"),
                () -> System.out.println("Error saving route")
        );
    }
}
