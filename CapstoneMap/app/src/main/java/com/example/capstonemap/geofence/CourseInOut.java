package com.example.capstonemap.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class CourseInOut extends BroadcastReceiver {
    private static final String TAG = "CourseInOut";

    private final GeoFenceListener geoFenceListener;

    public CourseInOut(GeoFenceListener geoFenceListener) {
        this.geoFenceListener = geoFenceListener;
    }

    // GeofenceManager 클래스는 지오펜스를 설정하는 것인데 여기서 시작점, 끝점의 좌표를 넣고 2개 생성하면
    // CourseInOut클래스에서 그 두개를 각각 GEOFENCE_TRANSITION_ENTER와 GEOFENCE_TRANSITION_EXIT로 받는다.
    // 각각 루트에 시작점에 왔을 때, 끝점에 갔을때를 의미한다.
    // CourseInOut의 그부분의 이벤트를 지정해주면 된다.
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing error: " + geofencingEvent.getErrorCode());
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d(TAG, "Geofence Entered");
            geoFenceListener.onGeofenceEnter();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "Geofence Exited");
            geoFenceListener.onGeofenceExit();
        } else {
            Log.e(TAG, "Unknown geofence transition: " + geofenceTransition);
        }
    }
}