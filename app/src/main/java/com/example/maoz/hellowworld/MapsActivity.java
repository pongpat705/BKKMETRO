package com.example.maoz.hellowworld;


import android.content.Context;

import android.graphics.Color;


import android.os.StrictMode;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends navigation_drawer implements GoogleMap.OnMapLongClickListener {

    GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Double curLat = 0.0, curLng =0.0; //เซทดีฟ้อล ไว้
    Marker Pin;
    Polyline polyline;
    GMapV2GetRouteDirection v2GetRouteDirection;


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
            public boolean onMarkerClick(Marker marker) {///ถ้าคลิกแล้วตำแหน่งเท่ากับ ม หัวเฉียว ให้ทำอะไร

                double lat = marker.getPosition().latitude;
                double lng = marker.getPosition().longitude;
                double desLat = lat;
                double desLng = lng;
                //drawLine(lat,lng); //เรียกวาดเส้น จากตำแหน่งปัจจุบันไปยังตำแหน่งที่คลิก
                exeProcess(desLat,desLng);

                return false;
            }

        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (appLocationManager.location()==null) {
                    //mMap.getMyLocation().getLatitude(); //เอาค่าจากปุ่ม MyLocation ไปใส่ใน Location ต้องรีเซตก่อนแทนค่า
                    if (mMap.getMyLocation() != null){
                        Log.d("----MyLoButton----", "take from mMap" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude() + "");
                        setMyMarker(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude(),mMap.getCameraPosition().zoom);
                        //ล้างค่า เพื่อทดสอบการเอาค่ามาจากปุ่ม MyLocation
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

         mMap.setMyLocationEnabled(true);//สร้างปุ่ม
         mMap.getUiSettings().setCompassEnabled(false);
         mMap.getUiSettings().setRotateGesturesEnabled(false);

         String url="http://192.168.43.144:80/android_connect/phpConnect1.php";
         JSONArray contacts;
         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);
         String jsonStr =  JSONParsing.readJSONFeed(url);

         Log.d("Responde: ", "> " + jsonStr);

         if (jsonStr != null) {
             try {
                 JSONObject jsonObj = new JSONObject(jsonStr);
                 // Getting JSON Array node
                 contacts = jsonObj.getJSONArray("result");
                 // looping through All Contacts
                 for (int i = 0; i < contacts.length(); i++) {
                     JSONObject c = contacts.getJSONObject(i);

                     String name = c.getString("station_name");
                     String lat = c.getString("station_lat");
                     String lng = c.getString("station_lng");
                     mMap.addMarker(new MarkerOptions()
                             .position(new LatLng(Double.valueOf(lat),Double.valueOf(lng)))
                             .title(name)//ไตเติ้ลหมุด
                             .snippet("Lat"+lat+"Lng"+lng));

                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         } else {
             Log.e("ServiceHandler", "Couldn't get any data from the url");
         }


     }
     /*
    * Function Group Setting  wait for edit
    *
    *
    * */

    private void setting(){
        ////test route
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        /*SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();*/
        ////
        mMap.setOnMapLongClickListener(this);
        Pin = mMap.addMarker(new MarkerOptions().position(appLocationManager.getLatLng()));

    }
    private void setMyMarker(double lat,double lng,double zoom) {//เปลี่ยนตำแหน่ง Marker
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

    private void drawLine(Double e1,Double e2){
        PolylineOptions line = new PolylineOptions();
        line.add(new LatLng(curLat, curLng)).add(new LatLng(e1,e2)).color(Color.RED).describeContents();//วาดเส้น
        mMap.addPolyline(line);//เอาเส้นที่วาดไปลงในแผนที่
    }

    private void exeProcess(double desLat,double desLng){ //วาดเส้นทางจากจุด A ไปจุด B
        String d;
        getPathWalk j = new getPathWalk();
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

