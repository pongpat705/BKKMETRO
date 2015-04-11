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

import java.util.ArrayList;
import java.util.HashMap;


public class Directions extends navigation_drawer {
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_directions, null, false);

        ArrayList<String> arrayPath = new ArrayList<>();
        arrayPath = (ArrayList<String>) getIntent().getExtras().get("arrayPath");

        drawerLayout.addView(contentView, 0);
        listView = (ListView)findViewById(R.id.direction_list);
        preparingList(arrayPath);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_directions, menu);
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
    private void preparingList(ArrayList<String> arrayPath){
        // looping through All Contacts
        ArrayList<HashMap<String,String>> direction_collection;
        direction_collection = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < arrayPath.size(); i++) {


            // tmp hashmap for single contact
            HashMap<String, String> direction = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            direction.put("station", arrayPath.get(i));
            direction.put("status","ss");

            // adding contact to contact list
            direction_collection.add(direction);
            // setupList
            ListAdapter adapter = new SimpleAdapter(Directions.this, direction_collection,
                    R.layout.direction_row, new String[] { "status","station"}, new int[] { R.id.status,R.id.stations});
            // setList follow prepare
            listView.setAdapter(adapter);
        }
    }
}