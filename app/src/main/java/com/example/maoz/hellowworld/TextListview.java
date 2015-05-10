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
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * หน้าแสดงลำดับการเดินทาง
 */

public class TextListview extends PublicTransport {
    private ListView listView;
    private TextView waypoint_head;
    String stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_waypoint, null, false);
        drawerLayout.addView(contentView, 0);
        listView = (ListView)findViewById(R.id.waypoint_list);
        waypoint_head = (TextView)findViewById(R.id.waypoint_head);
        LatLng desLatLng = (LatLng) getIntent().getExtras().get("desLatLng");
        stationName = (String) getIntent().getExtras().get("station");
        getDirections(desLatLng);
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
    /**ไปเอาข้อมูลจาก Directions API และสังให้สร้างวิว
     * @param desLatLng ค่าพิกัดปลายทาง
     * */
    private void getDirections(LatLng desLatLng){
        String d;
        JSONRouteWaypoint j = new JSONRouteWaypoint();
        d = j.getPath(new LatLng(appLocationManager.getLatitude(), appLocationManager.getLongitude()), desLatLng);
        ArrayList<HashMap<String,String>> arrayDirection = j.getWaypoint(d);

        setupList(arrayDirection);

    }
    /**สร้างรายการข้อมูลจาก Direction API
     * @param arrayPath ข้อมูลจาก Direction API
     * */
    private void setupList(ArrayList<HashMap<String, String>> arrayPath){
        // looping through All Contacts
        ArrayList<HashMap<String,String>> waypointCollection;
        waypointCollection = new ArrayList<>();
        for (int i = 0; i < arrayPath.size(); i++) {
            String direction = arrayPath.get(i).get("direction");
            String distance = arrayPath.get(i).get("distance");
            if (i == 0){
                waypoint_head.setText(arrayPath.get(i).get("distance")+" to "+stationName+"\n by follow directions");
            }else{
                HashMap<String, String> waypoint = new HashMap<String, String>();
                waypoint.put("instruction","["+(i)+"] "+direction);
                waypoint.put("distance",distance);
                waypointCollection.add(waypoint);
            }

        }
        // setupList
        ListAdapter adapter = new SimpleAdapter(TextListview.this, waypointCollection,
                R.layout.waypoint_row, new String[] {"instruction","distance"}, new int[] { R.id.instruction,R.id.distance});
        // setList follow prepare
        listView.setAdapter(adapter);


    }
}
