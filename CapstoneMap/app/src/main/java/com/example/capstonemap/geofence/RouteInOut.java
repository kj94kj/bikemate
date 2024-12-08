package com.example.capstonemap.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.capstonemap.MapsActivity;
import com.example.capstonemap.locationUpdate.UserUpdateInfo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class RouteInOut extends BroadcastReceiver {
    private static final String TAG = "RouteInOut";

    private final GeoFenceListener geoFenceListener;

    // GeoFenceListener를 받도록 생성자 추가
    public RouteInOut(GeoFenceListener geoFenceListener) {
        this.geoFenceListener = geoFenceListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing error: " + geofencingEvent.getErrorCode());
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d(TAG, "Entered Geofence");

            // 시작 이벤트
            geoFenceListener.onGeofenceEnter();

            Toast.makeText(context, "Entered the Route", Toast.LENGTH_SHORT).show();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "Exited Geofence");

            // 종료 이벤트
            geoFenceListener.onGeofenceExit();

            Toast.makeText(context, "Exited the Route", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Unknown geofence transition: " + geofenceTransition);
        }
    }
}