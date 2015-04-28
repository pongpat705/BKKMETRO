package com.example.maoz.hellowworld;


import android.content.Context;

import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * หน้าแผนที่
 */

public class MapView extends navigation_drawer{

    GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Marker pinSource,pinDestination;
    Polyline polyline;
    Double latDes,lngDes;
    String stationDes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_maps, null, false);
        drawerLayout.addView(contentView, 0);
        latDes = Double.valueOf((String) getIntent().getExtras().get("lat"));
        lngDes = Double.valueOf((String) getIntent().getExtras().get("lng"));
        stationDes = (String) getIntent().getExtras().get("station");
        setUpMapIfNeeded();
        exeProcess(latDes,lngDes);

        //Listener

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (appLocationManager.location()==null) {
                    if (mMap.getMyLocation() != null){
                        Log.d("----MyLoButton----", "take from mMap" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude() + "");
                        setMyMarker(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude(),mMap.getCameraPosition().zoom);
                        if(mMap.getCameraPosition().zoom !=15){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15));
                        }
                    }else MyToast("Waiting Location");
                } else{
                    setMyMarker(appLocationManager.getLatitude(),appLocationManager.getLongitude(),mMap.getCameraPosition().zoom);
                    if(mMap.getCameraPosition().zoom !=15){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(appLocationManager.getLatitude(), appLocationManager.getLongitude()), 15));
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        Log.d("----DEBUG----", "On Resume worked");
    }
    @Override
    protected void onPause() {
        super.onPause();
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
     private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
     /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
     private void setUpMap() {

         mMap.setMyLocationEnabled(true);//enable Location button
         mMap.getUiSettings().setCompassEnabled(false);
         mMap.getUiSettings().setRotateGesturesEnabled(false);
         pinSource = mMap.addMarker(new MarkerOptions().position(appLocationManager.getLatLng()));
         pinDestination = mMap.addMarker(new MarkerOptions()
                 .position(new LatLng(latDes,lngDes))
                 .title(stationDes)
                 .snippet("Destination " +latDes+ "," +lngDes));

         //preparing bounds marker ตีกรอบให้แสดงผลเส้นทางระหว่างจุดมาร์คสองจุด
         LatLngBounds.Builder b = new LatLngBounds.Builder();
         b.include(pinDestination.getPosition());
         b.include(pinSource.getPosition());
         LatLngBounds bounds = b.build();
         CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,700,700,20);
         mMap.moveCamera(cu);
         /**
          * this is for loading data stationlist from database*/
         /*for (int i = 0; i<stationList.size();i++){
             mMap.addMarker(new MarkerOptions()
             .position(new LatLng(stationList.get(i).getLat(),stationList.get(i).getLng()))
             .title(stationList.get(i).getStations())
             .snippet("Lat" + stationList.get(i).getLat() + "Lng" + stationList.get(i).getLng()));
         }*/

     }
     /*
    * Function Group Setting  wait for edit
    *
    *
    * */

    private void setMyMarker(double lat,double lng,double zoom) {//change lat,lng Marker
        pinSource.setPosition(new LatLng(lat, lng));
        pinSource.setTitle("ตำแหน่งของฉัน");
        pinSource.setSnippet("Lat:" + lat + "Long:" + lng + " zoom level" + zoom);
        pinSource.showInfoWindow();
    }


    /*
    *
    * Calculate Function*/

    private void exeProcess(double desLat,double desLng){ //draw line a to b
        String d;
        JSONRouteMapView j = new JSONRouteMapView();
        d = j.getPath(new LatLng(appLocationManager.getLatitude(), appLocationManager.getLongitude()), new LatLng(desLat, desLng));
        List<List<HashMap<String, String>>> line = j.GetLine(d);
        if (polyline == null){
            polyline = mMap.addPolyline(Drawline(line));
        }else {
            polyline.remove();
            polyline = mMap.addPolyline(Drawline(line));
        }

    }
    private PolylineOptions Drawline(List<List<HashMap<String, String>>> result){
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        String distance = "";
        String duration = "";

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<>();
            lineOptions = new PolylineOptions().color(Color.BLUE).width(9);

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);
                if(j==0){    // Get distance from the list
                    distance = point.get("distance");
                    continue;
                }else if(j==1){ // Get duration from the list
                    duration = point.get("duration");
                    continue;
                }

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
        }
        MyToast("NearbyStations:" + distance + ", Duration:" + duration);

        // Drawing polyline in the Google Map for the i-th route
        return lineOptions;

    }


}

