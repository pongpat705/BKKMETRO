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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;


public class WaypointListview extends navigation_drawer {
    private ListView listView;
    LatLng desLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_waypoint, null, false);
        drawerLayout.addView(contentView, 0);
        listView = (ListView)findViewById(R.id.waypoint_list);
        desLatLng = (LatLng) getIntent().getExtras().get("desLatLng");
        getWaypoint();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waypoint, menu);
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
    private void getWaypoint(){
        String d;
        JSONRouteWaypoint j = new JSONRouteWaypoint();
        d = j.getPath(new LatLng(appLocationManager.getLatitude(), appLocationManager.getLongitude()), desLatLng);
        ArrayList<String> waypoint = j.getWaypoint(d);

        preparingList(waypoint);

    }
    private void preparingList(ArrayList<String> arrayPath){
        // looping through All Contacts
        ArrayList<HashMap<String,String>> waypointCollection;
        waypointCollection = new ArrayList<>();
        for (int i = 0; i < arrayPath.size(); i++) {
            String direction = arrayPath.get(i);

            HashMap<String, String> waypoint = new HashMap<String, String>();
            waypoint.put("instruction",direction);
            waypointCollection.add(waypoint);
        }
        // setupList
        ListAdapter adapter = new SimpleAdapter(WaypointListview.this, waypointCollection,
                R.layout.waypoint_row, new String[] {"instruction"}, new int[] { R.id.instruction});
        // setList follow prepare
        listView.setAdapter(adapter);
    }
}
