package com.example.maoz.hellowworld;


import android.content.Context;

import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MapsActivity extends navigation_drawer implements GoogleMap.OnMapLongClickListener {

    GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Double curLat = 0.0, curLng =0.0; //set default
    Marker Pin;
    Polyline polyline;
    XMLRoutDirection v2GetRouteDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_maps, null, false);
        drawerLayout.addView(contentView, 0);
        setUpMapIfNeeded();
        setting();

        //Listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("ตำแหน่งของฉัน"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),10));//ซุมแม่ง
                return false;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {//

                double lat = marker.getPosition().latitude;
                double lng = marker.getPosition().longitude;
                exeProcess(lat, lng);

                return false;
            }

        });

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
                    }else setMyToast("Waiting Location");
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
    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You Click here")
                .snippet("lat "+latLng.latitude+" long "+latLng.longitude));
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

         for (int i = 0; i<stationList.size();i++){
             mMap.addMarker(new MarkerOptions()
             .position(new LatLng(stationList.get(i).getLat(),stationList.get(i).getLng()))
             .title(stationList.get(i).getStations())
             .snippet("Lat" + stationList.get(i).getLat() + "Lng" + stationList.get(i).getLng()));
         }



     }
     /*
    * Function Group Setting  wait for edit
    *
    *
    * */

    private void setting(){
        ////test route
        v2GetRouteDirection = new XMLRoutDirection();
        /*SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();*/
        ////
        mMap.setOnMapLongClickListener(this);
        Pin = mMap.addMarker(new MarkerOptions().position(appLocationManager.getLatLng()));

    }
    private void setMyMarker(double lat,double lng,double zoom) {//change lat,lng Marker
        Pin.setPosition(new LatLng(lat,lng));
        Pin.setTitle("ตำแหน่งของฉัน");
        Pin.setSnippet("Lat:"+ lat +"Long:"+ lng+" zoom level"+zoom);
        Pin.showInfoWindow();
    }
    private void setMyToast(String string) {
        LayoutInflater inflater = getLayoutInflater();
        View Layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textView = (TextView) Layout.findViewById(R.id.text);
        textView.setText(string);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(Layout);
        toast.show();

    }

    /*
    *
    * Calculate Function*/

    private void drawLine(Double e1,Double e2){ //Draw Line on map
        PolylineOptions line = new PolylineOptions();
        line.add(new LatLng(curLat, curLng)).add(new LatLng(e1,e2)).color(Color.RED).describeContents();//วาดเส้น
        mMap.addPolyline(line);
    }

    private void exeProcess(double desLat,double desLng){ //draw line a to b
        String d;
        JSONRoutDirection j = new JSONRoutDirection();
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
        setMyToast("Distance:" + distance + ", Duration:" + duration);

        // Drawing polyline in the Google Map for the i-th route
        return lineOptions;

    }


}

