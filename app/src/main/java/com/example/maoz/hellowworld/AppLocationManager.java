package com.example.maoz.hellowworld;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by jitsuem on 2/1/2015.
 */
public class AppLocationManager implements LocationListener {

    private Location location;
    private double latitude;
    private double longitude;
    boolean isGPSEnabled = false; // ตัวแปรเช็คว่าเปิดหรือปิด
    boolean isNetworkEnabled = false;
    private boolean Enable = false;


    public AppLocationManager(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //เปิดหรือปิด
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled){
            Log.d("----Please----", "Turn on Location");
            Enable = false;
        }else{
            Enable = true;
            if (isGPSEnabled){//ยังมีปัญหาอยู่ แก้ไขลำดับSyntax
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);//get value from GPS
               Log.d("----GPS----","requesting");
               if (locationManager != null){ //โอเคเอาค่าล่าสุดที่รู้ไปให้ Location จาก GPS
                  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                  if (location == null){
                      Log.d("----GPS null----", ":null");
                  }else{
                      setMostRecentLocation(location); //ปรับปรุงค่า Lat Long
                      Log.d("----GPS not null----", ":" + getLatitude() + "," + getLongitude() + "");
                  }

               }

            }else if (isNetworkEnabled){ // ถ้าเปิดให้ไปร้องขอตำแหน่ง
                      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 10, this);
                      Log.d("----Network----","reqLocation");
                      if (locationManager != null){//โอเคเอาค่าล่าสุดที่รู้ไปให้ Location จาก NETWORK
                          location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                          if (location == null){
                              Log.d("----Network null----", ":null");
                          }else{
                              setMostRecentLocation(location); //ปรับปรุงค่า Lat Long
                              Log.d("----not null----", "Network" + getLatitude() + "," + getLongitude() + "");
                          }

                      }
            }

        }

    }


    private void setMostRecentLocation(Location lastKnownLocation) {
        latitude = lastKnownLocation.getLatitude();
        longitude = lastKnownLocation.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng(){
        LatLng latLng = new LatLng(latitude,longitude);
        return latLng;
    }

    public Location location(){
        return location;
    }
    public Boolean isEnabled(){
        return Enable;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        setMostRecentLocation(location);
        Log.d("----Apploca----","UPDATED");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }






}
