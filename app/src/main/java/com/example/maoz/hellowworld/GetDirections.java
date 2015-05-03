package com.example.maoz.hellowworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
/**
 * หน้าค้นหาลำดับการเดินทางโดยให้เลือกเงื่อนไข
 */
public class GetDirections extends PublicTransport {

    static ArrayList<String> spinStations = new ArrayList<String>();
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button searchButton;
    ArrayList<String> arrayPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ประกาศทุกหน้าที่เป็น หน้าลูก
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_my, null, false);
        drawerLayout.addView(contentView, 0);
        gettingData();
        searchButton = (Button) findViewById(R.id.button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton)findViewById(checkedId);
                MyToast((String) radioButton.getText());
            }
        });
        if (spinStations.isEmpty()){
            for(int i=0;i<stationList.size();i++) {//insert data from database to list here
                spinStations.add(stationList.get(i).getStations());
            }
        }

        final Spinner stationtop = (Spinner)findViewById(R.id.spinner);
        final Spinner stationdown = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> spintop = new ArrayAdapter<String>(this, R.layout.spinner_item, spinStations);
        ArrayAdapter<String> spindown = new ArrayAdapter<String>(this, R.layout.spinner_item, spinStations);
        stationtop.setAdapter(spintop);
        stationdown.setAdapter(spindown);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadio = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedRadio);
                int conditions =  radioButton.getId();
                String source = stationtop.getSelectedItem().toString();
                String destination = stationdown.getSelectedItem().toString();
                arrayPath = CalculateShortestPath(source,destination,conditions);

                Intent direction = new Intent(GetDirections.this,DirectionsListview.class);
                direction.putExtra("arrayPath",arrayPath);
                startActivity(direction); // call new Activity


            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
