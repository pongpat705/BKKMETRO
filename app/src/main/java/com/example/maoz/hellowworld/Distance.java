package com.example.maoz.hellowworld;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;



public class Distance extends navigation_drawer {
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
        listView = (ListView)findViewById(R.id.list);
        preparingList();

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
        ArrayList<HashMap<String,String>> contactList;
        contactList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < stationList.size(); i++) {

            String name = stationList.get(i).getStations();
            double lat = stationList.get(i).getLat();
            double lng = stationList.get(i).getLng();

            String distance = String.valueOf(df2.format(FindDistance.getDistance(appLocationManager.getLatitude(), appLocationManager.getLongitude(),lat, lng)));

            // tmp hashmap for single contact
            HashMap<String, String> contact = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            contact.put("station_name", "สถานี "+name);
            contact.put("distance","ห่างจากคุณ "+distance+" กิโลเมตร");

            // adding contact to contact list
            contactList.add(contact);
            // setupList
            ListAdapter adapter = new SimpleAdapter(Distance.this, contactList,
                    R.layout.row, new String[] { "station_name","distance"}, new int[] { R.id.stations,R.id.distance});
            // setList follow prepare
            listView.setAdapter(adapter);
        }
    }

}