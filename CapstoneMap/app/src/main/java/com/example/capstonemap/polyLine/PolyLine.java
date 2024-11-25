    package com.example.capstonemap.polyLine;


    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.Executors;
    import java.util.HashMap;

    import android.graphics.Color;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;

    import com.example.capstonemap.MakeApiRequest.MakeApiRequest;
    import com.example.capstonemap.BuildConfig;
    import com.example.capstonemap.MapsActivity;
    import com.example.capstonemap.routes.RouteRepository;
    import com.google.android.gms.maps.model.LatLng;
    import com.google.android.gms.maps.model.Polyline;
    import com.google.android.gms.maps.model.PolylineOptions;

    import org.json.JSONArray;
    import org.json.JSONObject;

    // 폴리라인을 길에 맞춰 그리는 함수 구현

    //폴리라인은 encodedString을 가져와 List<LatLng>으로 만든다음 그점들을 이어 그리는 것이다.
    public class PolyLine {
        RouteRepository routeRepository=new RouteRepository();

        //폴리라인의 정보를 담는 해시맵
        private static final HashMap<String, Polyline> polylineMap = new HashMap<>();
        private static String lastEncoded;

        // url에 응답을 요청하는 함수
        public static void getDirections(LatLng origin, LatLng destination) {
            String apiKey = BuildConfig.MAPS_API_KEY;
            String urlString = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin.latitude + "," + origin.longitude +
                    "&destination=" + destination.latitude + "," + destination.longitude +"&key=" + apiKey;

            Log.d("DIRECTIONS_API_REQUEST", "Request URL: " + urlString);

            Executors.newSingleThreadExecutor().execute(() -> {
                String response = MakeApiRequest.makeApiRequest(urlString);
                if (response != null) {
                    Log.d("DIRECTIONS_API_RESPONSE", response);
                    new Handler(Looper.getMainLooper()).post(() -> drawPolyline(response));
                }else{
                    Log.e("DIRECTIONS_API", "No response from the API");
                }
            });
        }

        // 거리, 폴리라인 그리기, 시작점 등등의 정보를 확인가능하다
        private static void drawPolyline(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray routes = jsonObject.getJSONArray("routes");
                List<LatLng> path = new ArrayList<>();

                //로그 추가 zero_results 반환때문에
                String status = jsonObject.getString("status");
                Log.d("DIRECTIONS_API_STATUS", "API Response Status: " + status);

                if (!"OK".equals(status)) {
                    Log.e("DIRECTIONS_API_STATUS", "Error: API response status is " + status);
                    if ("ZERO_RESULTS".equals(status)) {
                        Log.e("DIRECTIONS_API", "No routes found between the specified origin and destination.");
                    }
                    return;
                }

                //경로 데이터를 파싱함

                if (routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    JSONArray legs = route.getJSONArray("legs");

                    // 여기서 거리, 폴리라인 그리기, 시작점 등등의 정보를 확인가능하다

                    if (legs.length() > 0) {
                        JSONObject leg = legs.getJSONObject(0);

                        JSONObject startLocation = leg.getJSONObject("start_location");
                        LatLng startPoint = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                        Log.d("ROUTE_INFO", "Start Point: " + startPoint);

                        JSONObject endLocation = leg.getJSONObject("end_location");
                        LatLng endPoint = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));
                        Log.d("ROUTE_INFO", "End Point: " + endPoint);

                        JSONObject distance = leg.getJSONObject("distance");
                        String distanceText = distance.getString("text"); // 사람이 읽기 쉬운 거리
                        int distanceValue = distance.getInt("value"); // 거리 값 (미터 단위)
                        Log.d("ROUTE_INFO", "Distance: " + distanceText + " (" + distanceValue + " meters)");

                        JSONObject duration = leg.getJSONObject("duration");
                        String durationText = duration.getString("text"); // 사람이 읽기 쉬운 소요 시간
                        int durationValue = duration.getInt("value"); // 소요 시간 값 (초 단위)
                        Log.d("ROUTE_INFO", "Duration: " + durationText + " (" + durationValue + " seconds)");

                        // Polyline 경로 그리기
                        JSONArray steps = leg.getJSONArray("steps");
                        for (int j = 0; j < steps.length(); j++) {
                            String polyline = steps.getJSONObject(j).getJSONObject("polyline").getString("points");
                            path.addAll(decodePolyline(polyline));
                        }
                    }
                }

                // polyline과 encoded는 폴리라인의 정보를 저장하기 위함임
                // 여기서 옵션을 건드리면 색깔과 굵기를 바꿀 수 있음
                PolylineOptions polylineOptions = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                Polyline polyline=MapsActivity.getMap().addPolyline(polylineOptions);
                String encodedPath = jsonObject.getJSONArray("routes").getJSONObject(0)
                        .getJSONObject("overview_polyline").getString("points");
                lastEncoded=encodedPath;

                polylineMap.put(encodedPath, polyline);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //인코딩된 String 폴리라인을 latlng로 변환
        private static List<LatLng> decodePolyline(String encoded) {
            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((lat / 1E5), (lng / 1E5));
                poly.add(p);
            }

            return poly;
        }

        // 해시맵에서는 제거 안하게만듦
        // 특정 encoded 경로를 기반으로 지도에서만 폴리라인 삭제
        public static void removePolyline(String encodedPath) {
            // 저장된 Polyline 객체를 가져와 지도에서 제거
            Polyline polyline = polylineMap.get(encodedPath);
            if (polyline != null) {
                polyline.remove();  // 지도에서 폴리라a인 제거
                Log.d("REMOVE_POLYLINE", "Polyline removed for path: " + encodedPath);
            } else {
                Log.e("REMOVE_POLYLINE", "No Polyline found for path: " + encodedPath);
            }
        }

        public static String getLastEncoded(){
            return lastEncoded;
        }

    }

