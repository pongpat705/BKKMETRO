package com.example.maoz.hellowworld;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
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

        String[] drawerListViewItems = getResources().getStringArray(R.array.items);
        drawerListView = (ListView)findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_listview_item, drawerListViewItems));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        appLocationManager = new AppLocationManager(this);
        if (!appLocationManager.isEnabled()){
            turnONGPS(this);
        }

        if (savedInstanceState == null){
            gettingData();
        }






        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mTitle);
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
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener, com.example.maoz.hellowworld.DrawerItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MyToast((String) ((TextView) view).getText());
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
                    Intent traininfo = new Intent(navigation_drawer.this,TrainInfo.class);
                    startActivity(traininfo); // call new Activity
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
        String station_url="http://gameparty.zapto.org:8989//android_connect/stations.php";
        String path_url="http://gameparty.zapto.org:8989//android_connect/paths.php";
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
                    int price = c.getInt("station_price");
                    int extd = c.getInt("station_extend");
                    Station_objects station_objects = new Station_objects(name,lat,lng,type,price,extd);
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

                    String type = c.getString("path_type");
                    Path_objects path_objects = new Path_objects(st_a,st_b,distance,type);
                    pathList.add(path_objects);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

    }
    /**หลังจากเช็คว่าเปิด gps หรือไม่ ถ้ายังเรียกมานี้
     * @param context หน้า activity
     * */
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

    /**คำนวณหาระยะทางจอง latlng a to latlng b
     * @param sLat ต้นทาง
     * @param sLng ต้นทาง
     * @param dLat ปลายทาง
     * @param dLng ปลายทาง
     * @return ค่าระยะ*/
    public Double calculateDistance(double sLat, double sLng, double dLat, double dLng){//คำนวณระยะทางขจัดระหว่างจุดสองจุดบนโลกใช้ HarvenSine
        double AVG_R_EARTH = 6371;

        double latDistance = Math.toRadians(sLat-dLat);
        double lngDistance = Math.toRadians(sLng-dLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance /2) + Math.cos(Math.toRadians(sLat))*Math.cos(Math.toRadians(dLat))* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return (double)Math.round(AVG_R_EARTH * c);
    }

    /**แสดงโทรส
     * @param string ค่าสตริงที่ต้องการให้โชว์โทรส*/
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

    /**คำนวณหาค่า ฮิวริสติก ของราคา โดยใช้การการเปลี่ยนสถานี
     * @param source ประเภทสถานีต้นทาง
     * @param destination ประเภทสถานีปลายทาง
     * @return ค่าเปลี่ยนสถานีของต้นทางกับปลายทาง
     * */
    private double PriceH(String source,String destination){//ฮิวริสติกด้านราคา
        double heuristic = 0.0;
        Map<String, Double> BTS = new HashMap<>();
        BTS.put("BTS", 0.0);
        BTS.put("MRT", 1.0);
        BTS.put("ARL", 1.0);
        BTS.put("BRT", 1.0);

        Map<String, Double> BRT = new HashMap<>();
        BRT.put("BRT", 0.0);
        BRT.put("BTS", 1.0);
        BRT.put("MRT", 2.0);
        BRT.put("ARL", 2.0);

        Map<String, Double> MRT = new HashMap<>();
        MRT.put("MRT", 0.0);
        MRT.put("BTS", 1.0);
        MRT.put("BRT", 2.0);
        MRT.put("ARL", 1.0);

        Map<String, Double> ARL = new HashMap<>();
        ARL.put("ARL", 0.0);
        ARL.put("BTS", 1.0);
        ARL.put("MRT", 1.0);
        ARL.put("BRT", 2.0);

        switch (source){
            case "BTS":
                    heuristic = BTS.get(destination);
                break;
            case "BRT":
                    heuristic = BRT.get(destination);
                break;
            case "MRT":
                    heuristic = MRT.get(destination);
                break;
            case "ARL":
                    heuristic = ARL.get(destination);
                break;

        }
        return heuristic;
    }

    /**คำนวณลำดับการเดินทาง
     * @param source ต้นทาง
     * @param destination ปลายทาง
     * @param type ประเภทเงื่อนไข เปลี่ยนสถานีน้อย หรือ ระยะทางสั้น
     * @return ลำดับการเดินทางในรูป ArrayList
     * */
    public ArrayList CalculateShortestPath(String source, String destination, String type){
        Map<String, Map<String, Double>> hueristic = new HashMap<>(); //เอาไว้เก็บฮิวริสติก
        ArrayList<String> arrayPath = new ArrayList<>(); //เอาไว้รีเทินลำดับการเดินทาง
        List<Map<String, Double>> list = new ArrayList<>();// ลิสของ map
        double distance;
        for (int i = 0; i<stationList.size();i++){//ลูปสำหรับข้อมูลต้นทาง
            Map<String, Double> map = new HashMap<>(); //map ของข้อมูล คุ่อันดับ สถานีและระยะขจัด
            for (int j = 0; j<stationList.size();j++){//ลุปสำหรับข้อมูลปลายทาง
                //ถ้าเงื่อนไขเป็นระยะทาง
                if (type.equals("Less Distance")){
                    double sLat,sLng,dLat,dLng,ans;
                    //ต้นทาง
                    sLat = stationList.get(i).getLat();
                    sLng = stationList.get(i).getLat();
                    //ปลายทาง
                    dLat = stationList.get(j).getLat();
                    dLng = stationList.get(j).getLat();
                    ans = calculateDistance(sLat, sLng, dLat, dLng);//ส่งไปคำนวณหาระยะทาง
                    //ใส่ข้อมูลระยะขจัดให้กับสถานี j
                    map.put(stationList.get(j).getStations(), ans);//กำหนด Heuristic ให้กับสถานี
                }else{//ถ้าเป็นราคา ใช้การเปลี่ยนสถานีน้อยที่สุดจะช่วยลดค่าใช้จ่าย
                    String[] s,d;
                    double ans;
                    //ต้นทาง
                    s = stationList.get(i).getType().split("_");
                    //ปลายทาง
                    d = stationList.get(j).getType().split("_");

                    ans = PriceH(s[0],d[0]);
                    map.put(stationList.get(j).getStations(),ans);
                }
            }
            //เพิ่มลงไปในลิสของ map
            list.add(map);
        }
        //System.out.println(list); เช็คว่ามีอะไรอยู่ในลิส

        Map[] maps = list.toArray(new HashMap[list.size()]);//ทดลองเรื่อง heuristic
        for (int i = 0; i<list.size();i++){
            //เอาค่าฮิวริสติกที่คำนวณไว้แล้วมาใส่ให้กับสถานี i จะมีข้อมูล list ช่องที่ i โดย list ช่องที่ i จะมีข้อมูลระยะขจัดของสถานี i เทียบกับทุกสถานี
            hueristic.put(String.valueOf(stationList.get(i).getStations()),maps[i]);
        }
        GraphAStar<String> graph = new GraphAStar<>(hueristic);//สร้างกราฟ AStar โดยกำหนดข้อมูล ฮิวริสติกไปด้วย
        //เพิ่มโหนดให้กราฟ
        for (int i = 0; i < stationList.size();i++){
            graph.addNode(String.valueOf(stationList.get(i).getStations()));//เอาชื่อสถานีไปเป็น node
        }

         //เพิ่มเส้นเชื่อมให้โหนดในกราฟ
         if (type.equals("Less Distance")){//ถ้าเป็นเงื่อนไขระยะทางเพิ่มแบบนี้
             for (int i = 0; i < pathList.size();i++){
               graph.addEdge(pathList.get(i).getStation_a(),pathList.get(i).getStation_b(),pathList.get(i).getDistance());//เพิ่มเส้นเชื่อมระหว่างสถานี
             }
         }else{//ถ้าเป็นเงื่อนไขราคาเพิ่มแบบนี้
             for (int i = 0; i < pathList.size();i++){

                if (pathList.get(i).getType().equals("WALK")){
                    graph.addEdge(pathList.get(i).getStation_a(),pathList.get(i).getStation_b(),1.0);
                }else{
                    graph.addEdge(pathList.get(i).getStation_a(),pathList.get(i).getStation_b(),0.0);
                }
             }
         }

        AStar<String> aStar = new AStar<>(graph);
        for (String path : aStar.astar(source, destination)) {
           arrayPath.add(path);
        }
        distance = aStar.distance;
        //เพิ่มอาเร ช่องสุดท้ายเป็นข้อมูลสรุป
        if (type.equals("Less Distance")){
            arrayPath.add(source + " to " + destination + " = " + Math.round(distance)+" kilometer");
        }else {
            arrayPath.add(source + " to " + destination);
        }


        return arrayPath;
    }



}
