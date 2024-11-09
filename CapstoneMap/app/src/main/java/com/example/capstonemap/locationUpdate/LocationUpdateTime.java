package com.example.capstonemap.locationUpdate;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

// 유저들의 정보(속도, 위치 등등) 간격을 얼마에 한번씩 업데이트 할 지 정함
public class LocationUpdateTime {
    public static LocationRequest locationRequestSetting(){
        // LocationRequest 생성
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // 1000초 간격 <- 비용문제
                .setMinUpdateIntervalMillis(1000) // 최소 업데이트 간격 500초 <- 비용때문에
                .build();

        return locationRequest;
    }
}