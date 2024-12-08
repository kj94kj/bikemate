package com.example.capstonemap.polyLine;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.capstonemap.BuildConfig;
import com.example.capstonemap.MakeApiRequest.MakeApiRequest;
import com.example.capstonemap.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class PolyLine {

    private static final HashMap<String, Polyline> polylineMap = new HashMap<>();
    private static String lastEncoded;

    // Directions API 호출
    public static void getDirections(LatLng origin, LatLng destination, OnEncodedPathReadyCallback callback) {
        String apiKey = BuildConfig.MAPS_API_KEY;
        String urlString = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&mode=transit&key=" + apiKey;

        Executors.newSingleThreadExecutor().execute(() -> {
            String response = MakeApiRequest.makeApiRequest(urlString);
            if (response == null) {
                Log.e("DIRECTIONS_API", "No response from API");
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(null));
                }
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status", "UNKNOWN");
                Log.d("DIRECTIONS_API_STATUS", "Status: " + status);

                if (!"OK".equals(status)) {
                    Log.e("DIRECTIONS_API_ERROR", "Status not OK: " + status);
                    if (callback != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(null));
                    }
                    return;
                }

                JSONArray routes = jsonObject.optJSONArray("routes");
                if (routes == null || routes.length() == 0) {
                    Log.e("DIRECTIONS_API_ERROR", "No routes found in the response.");
                    if (callback != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(null));
                    }
                    return;
                }

                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.optJSONObject("overview_polyline");
                String encodedPolyline = overviewPolyline != null ? overviewPolyline.optString("points") : null;

                if (encodedPolyline == null || encodedPolyline.isEmpty()) {
                    Log.e("DIRECTIONS_API_ERROR", "Encoded polyline is null or empty.");
                    if (callback != null) {
                        new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(null));
                    }
                } else {
                    Log.d("ENCODED_POLYLINE", "Encoded polyline: " + encodedPolyline);
                    lastEncoded = encodedPolyline;
                    new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(encodedPolyline));
                }
            } catch (Exception e) {
                Log.e("DIRECTIONS_API_ERROR", "Error parsing response", e);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onPathReady(null));
                }
            }
        });
    }

    // 지도에 폴리라인 그리기
    public static void drawPolylineOnMap(List<LatLng> path) {
        if (path == null || path.isEmpty()) {
            Log.e("DRAW_POLYLINE", "Path is null or empty, skipping drawing.");
            return;
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(path)
                .color(Color.BLUE)
                .width(5)
                .clickable(true);
        Polyline polyline = MapsActivity.getMap().addPolyline(polylineOptions);

        // Save the polyline for removal
        String encodedPath = encodePolyline(path);
        polylineMap.put(encodedPath, polyline);
        Log.d("DRAW_POLYLINE", "폴리라인 잘그려짐");
    }

    // 폴리라인 디코딩
    public static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        if (encoded == null || encoded.isEmpty()) {
            Log.e("DECODE_POLYLINE", "Encoded polyline is null or empty.");
            return poly;
        }

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

            LatLng point = new LatLng((lat / 1E5), (lng / 1E5));
            poly.add(point);
        }

        return poly;
    }

    // Encoded path 저장
    public static String getLastEncoded() {
        return lastEncoded;
    }

    // 폴리라인 제거
    public static void removePolyline(String encodedPath) {
        Polyline polyline = polylineMap.get(encodedPath);
        if (polyline != null) {
            polyline.remove();
            polylineMap.remove(encodedPath);
            Log.d("REMOVE_POLYLINE", "Polyline removed for path: " + encodedPath);
        } else {
            Log.e("REMOVE_POLYLINE", "No Polyline found for path: " + encodedPath);
        }
    }

    private static String encodePolyline(List<LatLng> path) {
        StringBuilder encoded = new StringBuilder();
        long lastLat = 0, lastLng = 0;

        for (LatLng point : path) {
            long lat = Math.round(point.latitude * 1E5);
            long lng = Math.round(point.longitude * 1E5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, encoded);
            encode(dLng, encoded);

            lastLat = lat;
            lastLng = lng;
        }

        return encoded.toString();
    }

    private static void encode(long value, StringBuilder encoded) {
        value = value < 0 ? ~(value << 1) : (value << 1);
        while (value >= 0x20) {
            encoded.append((char) ((0x20 | (value & 0x1f)) + 63));
            value >>= 5;
        }
        encoded.append((char) (value + 63));
    }

    // 모든 폴리라인 제거
    public static void removeAllPolylines() {
        if (polylineMap.isEmpty()) {
            Log.d("REMOVE_ALL_POLYLINES", "No polylines to remove.");
            return;
        }

        for (Polyline polyline : polylineMap.values()) {
            if (polyline != null) {
                polyline.remove();
            }
        }
        polylineMap.clear();
        Log.d("REMOVE_ALL_POLYLINES", "All polylines removed.");
    }
}

