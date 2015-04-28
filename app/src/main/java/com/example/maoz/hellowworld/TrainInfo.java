package com.example.maoz.hellowworld;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * หน้าแสดงข้อมูลระบบรถไฟฟ้าพร้อม ตารางเวลา
 */

public class TrainInfo extends navigation_drawer {
    public List<Trains_object> infolist = new ArrayList<>();
    static ArrayList<String> infospin = new ArrayList<String>();
    TextView textView;
    Button timeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ประกาศทุกหน้าที่เป็น หน้าลูก
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_train_info, null, false);
        drawerLayout.addView(contentView, 0);
        textView = (TextView)findViewById(R.id.detail);
        timeTable = (Button)findViewById(R.id.viewtimetable);

        if (infolist.isEmpty()){
            getTrainsinfo();
        }
        if (infospin.isEmpty()) {
            for (int i = 0; i < infolist.size(); i++) {//insert data from database to list here
                infospin.add(infolist.get(i).getName());
            }
        }

        final Spinner trainspinner = (Spinner)findViewById(R.id.trainspiner);
        ArrayAdapter<String> spintop = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, infospin);
        trainspinner.setAdapter(spintop);
        trainspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String check = (trainspinner.getSelectedItem().toString());
                for (int i = 0 ; i < infolist.size();i++){
                    if (check.equals(infolist.get(i).getName())){
                         textView.setText("Transportation name : " + infolist.get(i).getName() +
                                 "\nCoupon type : " + infolist.get(i).getCoupon() +
                                 "\nMinimum cost : " + infolist.get(i).getMin() + " THB" +
                                 "\nMaximum cost : " + infolist.get(i).getMax() + " THB" +
                                 "\nService time : " + infolist.get(i).getService() +
                                 "\nFrequency : " + infolist.get(i).getFrequency() + " minute");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText(trainspinner.getSelectedItem().toString());
            }
        });

        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = trainspinner.getSelectedItem().toString();
                switch (check){
                    case "BTS":
                        openWebURL("http://www.bts.co.th/customer/th/02-route-current_new.aspx");
                        break;
                    case "BRT":
                        openWebURL("http://www.bangkokbrt.com/main.php");
                        break;
                    case "MRT":
                        openWebURL("http://www.bangkokmetro.co.th");
                        break;
                    case "ARL":
                        openWebURL("http://www.deesudsud.com/wp-content/uploads/2014/12/apl_time_table.png");
                        break;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_train_info, menu);
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
    public void getTrainsinfo(){
        String info="http://gameparty.zapto.org:8989//android_connect/trainsinfo.php";
        JSONArray contacts;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // get stations
        String info_str  =  JSONParsing.readJSONFeed(info);
        Log.d("info: ", "> " + info_str);
        if (info_str != null) {
            try {
                JSONObject jsonObj = new JSONObject(info_str);
                // Getting JSON Array node
                contacts = jsonObj.getJSONArray("result");
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    int id = c.getInt("idtrainsinfo");
                    String name = c.getString("train_name");
                    String coupon = c.getString("train_coupon_type");
                    String min = c.getString("cost_min");
                    String max = c.getString("cost_max");
                    String service = c.getString("servicetime");
                    String frequency = c.getString("frequency");

                    Trains_object trains_object = new Trains_object(id,name,coupon,min,max,service,frequency);
                    //stationList.add(station_objects);
                    infolist.add(trains_object);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
    }

    public void openWebURL( String inURL ) {
        Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(inURL) );
        startActivity( browse );
    }
}
