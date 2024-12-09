package com.example.capstonemap.polyLine;

import static com.example.capstonemap.distance.DistancePolyLine.calculateTotalLength;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.example.capstonemap.MapsActivity;
import com.example.capstonemap.distance.DistancePolyLine;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.routes.RouteRepository;
import com.example.capstonemap.user.UserDto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClickPolyLine {
    static RouteRepository routeRepository=new RouteRepository();
    static RouteDto[] routeDto = new RouteDto[1];

    public static void clickPolyLine(GoogleMap mMap) {
        final LatLng[] origin = new LatLng[1];  // 출발지를 저장할 배열
        final LatLng[] destination = new LatLng[1];  // 도착지를 저장할 배열
        // final RouteDto[] routeDto = new RouteDto[1];
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

                    String[] locationDetails = getSubLocalityAndFeatureName(MapsActivity.getAppContext(), startLocation[0], startLocation[1]);
                    String subLocality = locationDetails[0];
                    String featureName = locationDetails[1];

                    String fullName=subLocality+" "+featureName;

                    routeDto[0] = new RouteDto(fullName, encodedPath, locationList, startLocation, length);

                    Gson gson = new Gson();
                    System.out.println(gson.toJson(routeDto[0]));

                    // saveRoute(routeDto, userDto);

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

    public static void saveRoute(UserDto userDto){
        Long userId= userDto.getId();
        System.out.println("User ID: " + userId);

        if(routeDto[0] != null){
            //리파지토리, 스프링과 연동해서 경로를 만들면 저장함
            routeRepository.saveRoute(routeDto[0], userId,
                    () -> System.out.println("Route saved successfully!"),
                    () -> System.out.println("Error saving route")
            );
        }else{
            System.out.println("null route");
        }
    }

    public static RouteDto getPolyLineRouteDto(){
        return routeDto[0];
    }

    public static void disablePolylineDrawing(GoogleMap map) {
        map.setOnMapLongClickListener(null);
    }

    private static String[] getSubLocalityAndFeatureName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String subLocality = address.getSubLocality(); // 구/군 이름
                String featureName = address.getFeatureName(); // 랜드마크 이름

                return new String[]{subLocality, featureName};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{"정보 없음", "정보 없음"};
    }
}
