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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class navigation_drawer extends FragmentActivity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public AppLocationManager appLocationManager;

    public List<Station_objects> stationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

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
                getActionBar().setTitle(mDrawerTitle);
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
            Toast.makeText(navigation_drawer.this, ((TextView) view).getText(), Toast.LENGTH_LONG).show();
            Fragment f;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            switch (position){
                case 0:
                    Intent main = new Intent(navigation_drawer.this,MyActivity.class);
                    startActivity(main); // call new Activity
                    break;
                case 1:
                    Intent distance = new Intent(navigation_drawer.this,Distance.class);
                    startActivity(distance); // call new Activity
                    break;
                case 2:
                    Intent map = new Intent(navigation_drawer.this,MapsActivity.class);
                    startActivity(map); // call new Activity
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
        String url="http://gameparty.zapto.org:8989//android_connect/phpConnect1.php";
        JSONArray contacts;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String station_str =  JSONParsing.readJSONFeed(url);
        Log.d("Respond-NavDrawer: ", "> " + station_str);

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



}
