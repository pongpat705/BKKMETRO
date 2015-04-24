package com.example.maoz.hellowworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    }

    public void mapviewClick(View v){
        RelativeLayout parentRow = (RelativeLayout)v.getParent();
        TextView station = (TextView)parentRow.getChildAt(1);
        TextView lat = (TextView)parentRow.getChildAt(4);
        TextView lng = (TextView)parentRow.getChildAt(5);

        Intent map = new Intent(NearbyStations.this,MapView.class);
        map.putExtra("station",station.getText());
        map.putExtra("lat",lat.getText());
        map.putExtra("lng",lng.getText());
        startActivity(map); // call new Activity
    }

    public void waypointClick(View v){
        RelativeLayout parentRow = (RelativeLayout)v.getParent();
        TextView stationName = (TextView)parentRow.getChildAt(1);
        TextView lat = (TextView)parentRow.getChildAt(4);
        TextView lng = (TextView)parentRow.getChildAt(5);
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
        ArrayList<HashMap<String,String>> station_collection = new ArrayList<>();
        ArrayList<HashMap<String,String>> bts_coll = new ArrayList<>();
        ArrayList<HashMap<String,String>> brt_coll = new ArrayList<>();
        ArrayList<HashMap<String,String>> mrt_coll = new ArrayList<>();
        ArrayList<HashMap<String,String>> arl_coll = new ArrayList<>();

        for (int i = 0; i < stationList.size(); i++) {

            String name = stationList.get(i).getStations();
            double lat = stationList.get(i).getLat();
            double lng = stationList.get(i).getLng();

            String distance = String.valueOf(df2.format(calculateDistance(appLocationManager.getLatitude(), appLocationManager.getLongitude(), lat, lng)));
            HashMap<String, String> station = new HashMap<>();
            HashMap<String, String> bts = new HashMap<>();
            HashMap<String, String> brt = new HashMap<>();
            HashMap<String, String> mrt = new HashMap<>();
            HashMap<String, String> arl = new HashMap<>();
            String[] splitType = stationList.get(i).getType().split("_");
            if (splitType[0].equals("BTS")){
                bts.put("station_name", name);
                bts.put("distance",distance );
                bts.put("lat",String.valueOf(lat));
                bts.put("lng",String.valueOf(lng));
                bts.put("IMAGE",String.valueOf(R.drawable.rulers100));
                bts_coll.add(bts);
            }else if (splitType[0].equals("BRT")){
                brt.put("station_name", name);
                brt.put("distance", distance);
                brt.put("lat",String.valueOf(lat));
                brt.put("lng",String.valueOf(lng));
                brt.put("IMAGE",String.valueOf(R.drawable.rulers100));
                brt_coll.add(brt);
            }else if (splitType[0].equals("MRT")){
                mrt.put("station_name", name);
                mrt.put("distance",distance );
                mrt.put("lat",String.valueOf(lat));
                mrt.put("lng",String.valueOf(lng));
                mrt.put("IMAGE",String.valueOf(R.drawable.rulers100));
                mrt_coll.add(mrt);
            }else {
                arl.put("station_name", name);
                arl.put("distance",distance );
                arl.put("lat",String.valueOf(lat));
                arl.put("lng",String.valueOf(lng));
                arl.put("IMAGE",String.valueOf(R.drawable.rulers100));
                arl_coll.add(arl);
            }


        }
        arraySort(bts_coll);
        arraySort(brt_coll);
        arraySort(mrt_coll);
        arraySort(arl_coll);
        station_collection.add(bts_coll.get(0));
        station_collection.add(brt_coll.get(0));
        station_collection.add(mrt_coll.get(0));
        station_collection.add(arl_coll.get(0));
        arraySort(station_collection);
        bts_coll.clear();
        brt_coll.clear();
        mrt_coll.clear();
        arl_coll.clear();


        // setupList
        ListAdapter adapter = new SimpleAdapter(NearbyStations.this, station_collection,
                R.layout.distance_row, new String[] {"IMAGE","station_name","distance","lat","lng"}, new int[] {R.id.list_far, R.id.stations,R.id.distance,R.id.lat,R.id.lng});
        // setList follow prepare
        listView.setAdapter(adapter);
    }
    public void arraySort(ArrayList<HashMap<String, String>> arrayList){
        Collections.sort(arrayList,new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                String a = lhs.get("distance");
                String b = rhs.get("distance");
                return a.compareTo(b);
            }
        });
    }

}