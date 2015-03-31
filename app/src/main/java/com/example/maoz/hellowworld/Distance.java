package com.example.maoz.hellowworld;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;



public class Distance extends navigation_drawer {
    private ListView listView;
    NumberFormat df2 = new DecimalFormat("###.#");
    JSONArray contacts = null;
    ArrayList<HashMap<String,String>> contactList;
    private static String url="http://gameparty.zapto.org:8989/android_connect/phpConnect1.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ประกาศทุกหน้าที่เป็น หน้าลูก
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_distance, null, false);
        drawerLayout.addView(contentView, 0);
        contactList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)findViewById(R.id.list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new GetContacts().execute(url);
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




private class GetContacts extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... url) {
        String jsonStr =  JSONParsing.readJSONFeed(url[0]);

        Log.d("Response: ", "> " + jsonStr);

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

                    String distance = String.valueOf(df2.format(FindDistance.getDistance(appLocationManager.getLatitude(), appLocationManager.getLongitude(), Double.valueOf(lat), Double.valueOf(lng))));


                    // tmp hashmap for single contact
                    HashMap<String, String> contact = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    contact.put("station_name", "สถานี "+name);
                    contact.put("station_lat", "ละติจูด "+lat);
                    contact.put("station_lng", "ลองจิจูด "+lng);
                    contact.put("distance", distance+" กิโลเมตร");

                    // adding contact to contact list
                    contactList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
                /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(Distance.this, contactList,
                R.layout.row, new String[] { "station_name", "station_lat",
                "station_lng","distance"}, new int[] { R.id.stations,
                R.id.lat, R.id.lng,R.id.distance});

        listView.setAdapter(adapter);
    }

}

}