package com.example.capstonemap.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.capstonemap.MapsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

//지오 펜스의 요청과 권한을 처리함
public class GeofenceManager {
    private static final String TAG = "GeofenceManager";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final GeofencingClient geofencingClient;
    private final Context context;

    public GeofenceManager(Context context) {
        this.context = context;
        this.geofencingClient = LocationServices.getGeofencingClient(context);
    }

    // 권한 확인 및 요청
    public boolean checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            if (context instanceof MapsActivity) { // MainActivity에서 실행될 경우
                ActivityCompat.requestPermissions(
                        (MapsActivity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE
                );
            }
            return false; // 권한이 없음
        }
        return true; // 권한이 있음
    }


    // 지오펜스를 설정하는 것인데 여기서 시작점, 끝점의 좌표를 넣고 2개 생성하면
    // CourseInOut클래스에서 그 두개를 각각 GEOFENCE_TRANSITION_ENTER와 GEOFENCE_TRANSITION_EXIT로 받는다.
    // radius는 범위
    // 각각 루트에 시작점에 왔을 때, 끝점에 갔을때를 의미한다.
    // CourseInOut의 그부분의 이벤트를 지정해주면 된다.
    public void setupGeofence(double enterLatitude, double enterLongitude, double exitLatitude, double exitLongitude, float radius) {
        if (!checkAndRequestPermissions()) {
            Log.e(TAG, "Permission not granted. Cannot setup geofence.");
            return;
        }

        // ENTER 지오펜스
        Geofence enterGeofence = new Geofence.Builder()
                .setRequestId("enterGeofence")
                .setCircularRegion(enterLatitude, enterLongitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();

        // EXIT 지오펜스
        Geofence exitGeofence = new Geofence.Builder()
                .setRequestId("exitGeofence")
                .setCircularRegion(exitLatitude, exitLongitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        // GeofencingRequest 생성
        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(enterGeofence)
                .addGeofence(exitGeofence)
                .build();

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofences added successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add geofences: " + e.getMessage()));
    }
    // 지오펜스 이벤트 처리
    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
    }

    // 권한 결과 처리
    public void handlePermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
            } else {
                Log.e(TAG, "Permission denied");
            }
        }
    }
}