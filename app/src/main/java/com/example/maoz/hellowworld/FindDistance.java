package com.example.maoz.hellowworld;

import android.location.Location;

/**
 * Created by jitsuem on 2/1/2015.
 */
public abstract class FindDistance {

    public static double getDistance(double curLat,double curLng,double targetLat,double targetLng){
        Location locationA = new Location("locationA");
        Location locationB = new Location("locationB");

        locationA.setLatitude(curLat);
        locationA.setLongitude(curLng);

        locationB.setLatitude(targetLat);
        locationB.setLongitude(targetLng);

        return (double) (locationA.distanceTo(locationB)/1000);
    }
}
