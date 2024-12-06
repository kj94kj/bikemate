package com.example.capstonemap.currentMapBound;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class SearchLocation {
    private final Context context;

    public SearchLocation(Context context) {
        this.context = context;
    }

    public LatLng getCoordinates(String locationName) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            } else {
                Log.e("SearchLocation", "Location not found: " + locationName);
                return null;
            }
        } catch (Exception e) {
            Log.e("SearchLocation", "Failed to get coordinates", e);
            return null;
        }
    }
}
