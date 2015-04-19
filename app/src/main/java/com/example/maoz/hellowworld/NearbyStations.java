package com.example.maoz.hellowworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;



public class NearbyStations extends navigation_drawer {
    private ListView listView;
    NumberFormat df2 = new DecimalFormat("###.#");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ประกาศทุกหน้าที่เป็น หน้าลูก
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_distance, null, false);
        drawerLayout.addView(contentView, 0);
        listView = (ListView)findViewById(R.id.distance_list);
        preparingList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                String ltln = item.get("lat")+","+item.get("lng");
                MyToast(ltln);

            }
        });

    }

    public void mapviewClick(View v){
        RelativeLayout parentRow = (RelativeLayout)v.getParent();
        TextView station = (TextView)parentRow.getChildAt(0);
        TextView lat = (TextView)parentRow.getChildAt(2);
        TextView lng = (TextView)parentRow.getChildAt(3);

        Intent map = new Intent(NearbyStations.this,MapView.class);
        map.putExtra("station",station.getText());
        map.putExtra("lat",lat.getText());
        map.putExtra("lng",lng.getText());
        startActivity(map); // call new Activity
    }

    public void waypointClick(View v){
        RelativeLayout parentRow = (RelativeLayout)v.getParent();
        TextView stationName = (TextView)parentRow.getChildAt(0);
        TextView lat = (TextView)parentRow.getChildAt(2);
        TextView lng = (TextView)parentRow.getChildAt(3);
        LatLng desLatLng = new LatLng(Double.valueOf((String) lat.getText()),Double.valueOf((String) lng.getText()));
        Intent waypoint = new Intent(NearbyStations.this,WaypointListview.class);
        waypoint.putExtra("desLatLng", desLatLng);
        waypoint.putExtra("station", stationName.getText());
        startActivity(waypoint); // call new Activity
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_distance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preparingList(){
        // looping through All Contacts
        ArrayList<HashMap<String,String>> station_collection;
        station_collection = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < stationList.size(); i++) {

            String name = stationList.get(i).getStations();
            double lat = stationList.get(i).getLat();
            double lng = stationList.get(i).getLng();

            String distance = String.valueOf(df2.format(calculateDistance(appLocationManager.getLatitude(), appLocationManager.getLongitude(), lat, lng)));

            // tmp hashmap for single contact
            HashMap<String, String> station = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            station.put("station_name", "สถานี " + name);
            station.put("distance", "ห่างจากคุณ " + distance + " กิโลเมตร");
            station.put("lat",String.valueOf(lat));
            station.put("lng",String.valueOf(lng));
            // adding contact to contact list
            station_collection.add(station);
        }

        // setupList
        ListAdapter adapter = new SimpleAdapter(NearbyStations.this, station_collection,
                R.layout.distance_row, new String[] { "station_name","distance","lat","lng"}, new int[] { R.id.stations,R.id.distance,R.id.lat,R.id.lng});
        // setList follow prepare
        listView.setAdapter(adapter);
    }

}