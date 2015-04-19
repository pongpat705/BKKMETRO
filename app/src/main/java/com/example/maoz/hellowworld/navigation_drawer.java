package com.example.maoz.hellowworld;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class navigation_drawer extends FragmentActivity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence mTitle;
    public AppLocationManager appLocationManager;

    public List<Station_objects> stationList = new ArrayList<>();
    public List<Path_objects> pathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        mTitle = getTitle().toString();

        drawerListViewItems = getResources().getStringArray(R.array.items);
        drawerListView = (ListView)findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item,drawerListViewItems));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        appLocationManager = new AppLocationManager(this);
        if (!appLocationManager.isEnabled()){
            turnONGPS(this);
        }
        gettingData();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener, com.example.maoz.hellowworld.DrawerItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MyToast((String) ((TextView) view).getText());
            Fragment f;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            switch (position){
                case 0:
                    Intent main = new Intent(navigation_drawer.this,SearchActivity.class);
                    startActivity(main); // call new Activity
                    break;
                case 1:
                    Intent distance = new Intent(navigation_drawer.this,NearbyStations.class);
                    startActivity(distance); // call new Activity
                    break;
                case 2:

                    break;
            }
            drawerLayout.closeDrawer(drawerListView);

        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
            getActionBar().setTitle(mTitle);
        }
    }

    private void gettingData(){
        String station_url="http://gameparty.zapto.org:8989//android_connect/phpConnect1.php";
        String path_url="http://gameparty.zapto.org:8989//android_connect/phpConnect2.php";
        JSONArray contacts;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // get stations
        String station_str =  JSONParsing.readJSONFeed(station_url);
        Log.d("stations: ", "> " + station_str);
        if (station_str != null) {
            try {
                JSONObject jsonObj = new JSONObject(station_str);
                // Getting JSON Array node
                contacts = jsonObj.getJSONArray("result");
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String name = c.getString("station_name");
                    double lat = Double.valueOf(c.getString("station_lat"));
                    double lng = Double.valueOf(c.getString("station_lng"));
                    String type = c.getString("station_type");
                    Station_objects station_objects = new Station_objects(name,lat,lng,type);
                    stationList.add(station_objects);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        // get path
        String path_str =  JSONParsing.readJSONFeed(path_url);
        Log.d("path: ", "> " + path_str);
        if (station_str != null) {
            try {
                JSONObject jsonObj = new JSONObject(path_str);
                // Getting JSON Array node
                contacts = jsonObj.getJSONArray("result");
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String st_a = c.getString("station_a");
                    String st_b = c.getString("station_b");
                    double distance = Double.valueOf(c.getString("distance"));
                    String getPrice = c.getString("price");
                    double price;
                    if ( getPrice == null || getPrice.length()==0){
                        price = 0;
                    }else{
                        price = Double.valueOf(getPrice);
                    }

                    String type = c.getString("path_type");
                    String detail = c.getString("path_detail");
                    Path_objects path_objects = new Path_objects(st_a,st_b,distance,price,type,detail);
                    pathList.add(path_objects);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

    }
    public void turnONGPS(Context context){
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please turn on Location service")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Show location settings when the user acknowledges the alert dialog
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    public Double calculateDistance(double sLat, double sLng, double dLat, double dLng){
        double AVG_R_EARTH = 6371;

        double latDistance = Math.toRadians(sLat-dLat);
        double lngDistance = Math.toRadians(sLng-dLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance /2) + Math.cos(Math.toRadians(sLat))*Math.cos(Math.toRadians(dLat))* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return (double)Math.round(AVG_R_EARTH * c);
    }
    public void MyToast(String string) {
        LayoutInflater inflater = getLayoutInflater();
        View Layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textView = (TextView) Layout.findViewById(R.id.text);
        textView.setText(string);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(Layout);
        toast.show();
    }
    public ArrayList CalculateShortestPath(String source, String destination){
        Map<String, Map<String, Double>> hueristic = new HashMap<String, Map<String, Double>>();
        ArrayList<String> arrayPath = new ArrayList<>();
        List<Map<String, Double>> list = new ArrayList<Map<String, Double>>();// ลิสของ map
        for (int i = 0; i<stationList.size();i++){
            //System.out.println(stationList.get(i).getStations() + "-" + stationList.get(i).getLat() + "-" + stationList.get(i).getLng());
            Map<String, Double> map = new HashMap<String, Double>(); //map ของข้อมูล คุ่อันดับ สถานีและระยะขจัด
            for (int j = 0; j<stationList.size();j++){
                double sLat,sLng,dLat,dLng,ans;
                sLat = stationList.get(i).getLat();
                sLng = stationList.get(i).getLat();
                dLat = stationList.get(j).getLat();
                dLng = stationList.get(j).getLat();
                ans = calculateDistance(sLat, sLng, dLat, dLng);//ส่งไปคำนวณหาระยะทาง
                map.put(stationList.get(j).getStations(), ans);//กำหนด Heuristic ให้กับสถานี
                //System.out.println(" "+i+" "+j);
            }
            list.add(map);
        }
        //System.out.println(list); เช็คว่ามีอะไรอยู่ในลิส

        Map[] maps = list.toArray(new HashMap[list.size()]);//ทดลองเรื่อง heuristic
        //System.out.print(maps[0].entrySet()); //ทดสอบว่า index 0 มีอะไรบ้าง
        for (int i = 0; i<list.size();i++){
            //System.out.println(maps[0]);
            hueristic.put(String.valueOf(stationList.get(i).getStations()),maps[i]);//ถูกแล้ว hueristic.put("A", mapA);
        }
        //System.out.println(hueristic);//ดูว่าอะไรอยู่ใน ฮิวริสติกบ้าง
        GraphAStar<String> graph = new GraphAStar<String>(hueristic);
        for (int i = 0; i < stationList.size();i++){
            graph.addNode(String.valueOf(stationList.get(i).getStations()));//เอาชื่อสถานีไปเป็น node
        }

        for (int i = 0; i < pathList.size();i++){
            //add edge here
            graph.addEdge(pathList.get(i).getStation_a(),pathList.get(i).getStation_b(),pathList.get(i).getDistance());//เพิ่มเส้นเชื่อมระหว่างสถานี
        }


        AStar<String> aStar = new AStar<String>(graph);
        for (String path : aStar.astar(source, destination)) {
           arrayPath.add(path);
        }
        arrayPath.add(source + " to " + destination + " = " + Math.round(aStar.distance)+" kilometer");

        return arrayPath;
    }



}
