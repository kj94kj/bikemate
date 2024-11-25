package com.example.capstonemap.distance;

import com.google.android.gms.maps.model.LatLng;

public class DistanceTwoLocation {

    static public LatLng doubleToLatLng(Double[] location){
        return new LatLng(location[0], location[1]);
    }

    // 나를 locationMe에 넣고 비교하는 상대를 locationOther에 넣는다. km 단위로 거리를 알려준다.
    static public double calculateDistance(LatLng point1, LatLng point2){
        final int EARTH_RADIUS = 6371000; // in meters

        double lat1 = Math.toRadians(point1.latitude);
        double lng1 = Math.toRadians(point1.longitude);
        double lat2 = Math.toRadians(point2.latitude);
        double lng2 = Math.toRadians(point2.longitude);

        double deltaLat = lat2 - lat1;
        double deltaLng = lng2 - lng1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
