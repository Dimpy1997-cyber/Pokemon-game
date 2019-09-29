package com.example.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class locationListener implements LocationListener {
    public static Location location;
    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        location= new Location("Start");
        location.setLatitude(0);
        location.setLongitude(0);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
