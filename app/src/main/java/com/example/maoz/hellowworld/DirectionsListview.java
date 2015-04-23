package com.example.maoz.hellowworld;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class DirectionsListview extends navigation_drawer {
    private ListView listView;
    private TextView headList;
    static final String STATUS = "status";
    static final String STATION = "station";
    static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_directions, null, false);
//get value from passing with Extra
        ArrayList<String> arrayPath;
        arrayPath = (ArrayList<String>) getIntent().getExtras().get("arrayPath");

        drawerLayout.addView(contentView, 0);
        listView = (ListView)findViewById(R.id.direction_list);
        headList = (TextView)findViewById(R.id.Direction_Head);


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
        // เอาไว้เก็บรายการแสดงผล
        ArrayList<HashMap<String,String>> direction_collection = new ArrayList<>();
        String typeTemp = null;
        int cost,
        btsCount = 0,
        btsCost = 0,
        btsExtd = 0,
        brtCount = 0,
        brtCost = 0,
        mrtCount = 0,
        mrtCost = 0,
        arlCount = 0,
        arlCost = 0;
        int btsCsum = 0,
                brtCsum = 0,
                mrtCsum = 0,
                arlCsum = 0;


        int lastIndex = arrayPath.size()-1;//ค่าสุดท้ายที่เพิ่มหลังสุด
        int b4lastIndex = arrayPath.size()-2;//สถานีสุดท้ายเป็น Destination

        for (int i = 0; i < arrayPath.size(); i++) {
            // tmp hashmap for single direction
            HashMap<String, String> direction = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            for (int j = 0; j<stationList.size();j++) {
                //เช้คว่าชื่อสถานีอันไหนตรงกับข้อมูลที่มีบ้าง เพื่อที่จะสามารถดึงเอา object ออกมาได้
                if (stationList.get(j).getStations().equals(arrayPath.get(i))) {
                    String type = stationList.get(j).getType();
                    String[] typeSplit = type.split("_");


                        if (i == 0){
                            //จัดการแสดงผล
                            direction.put(STATUS, "Start with "+stationList.get(j).getType());
                            direction.put(STATION, stationList.get(j).getStations());
                            direction.put(IMAGE,String.valueOf(R.drawable.start100));
                            typeTemp = type;
                        }else if (i == b4lastIndex){
                            direction.put(STATUS, "Destination is "+stationList.get(j).getType());
                            direction.put(STATION, stationList.get(j).getStations());
                            direction.put(IMAGE,String.valueOf(R.drawable.finish100));
                            typeTemp = type;
                        }else if (!type.equals(typeTemp)){
                            direction.put(STATUS, "InterChange to "+stationList.get(j).getType());
                            direction.put(STATION, stationList.get(j).getStations());
                            direction.put(IMAGE,String.valueOf(R.drawable.tranfer100));

                            if ((type.equals("BTS_SIL") && typeTemp.equals("BTS_SUK"))||(type.equals("BTS_SUK") && typeTemp.equals("BTS_SIL"))){
                            //ไม่ต้องทำไร
                            }else{
                                //init การนับ เพราะลงจากระบบแล้ว
                                btsCount = 0;
                                brtCount = 0;
                                mrtCount = 0;
                                arlCount = 0;
                                //เอาค่าใช้จ่ายไปเก็บ Sum เพราะลงจากระบบแล้ว init เป็น 0 ด้วย
                                btsCsum += btsCost;
                                btsCost = 0;
                                brtCsum += brtCost;
                                brtCost = 0;
                                mrtCsum += mrtCost;
                                mrtCost = 0;
                                arlCsum += arlCost;
                                arlCost = 0;
                            }

                            typeTemp = type;
                        }else if (type.equals(typeTemp)){
                            direction.put(STATUS, "Travel in "+stationList.get(j).getType());
                            direction.put(STATION, stationList.get(j).getStations());
                            direction.put(IMAGE,String.valueOf(R.drawable.train100));
                            typeTemp = type;
                        }

                    switch (typeSplit[0]){
                        case "BTS":
                            if (stationList.get(j).getExtd() == 1){
                                btsExtd = stationList.get(j).getPrice();
                            }else {
                                if (btsCount <= 1){
                                    btsCost = stationList.get(j).getPrice();
                                }else if (btsCount == 2){
                                    btsCost = 22;
                                }else if (btsCount > 2 && btsCount <8){
                                    btsCost = btsCost + 3;
                                }else {
                                    btsCost = 42;
                                }
                                btsCount++;
                            }

                            break;
                        case "BRT":
                            if (brtCount > 1){
                                brtCost = stationList.get(j).getPrice();
                            }else if (brtCount > 3){
                                brtCost = stationList.get(j).getPrice() + 2;
                            }else if (brtCount > 5){
                                brtCost = stationList.get(j).getPrice() + 4;
                            }else if (brtCount > 8){
                                brtCost = stationList.get(j).getPrice() + 6;
                            }else {
                                brtCost = stationList.get(j).getPrice() + 8;
                            }
                            brtCount++;
                            break;
                        case "MRT":
                            if (mrtCount <= 1){
                                mrtCost = stationList.get(j).getPrice();
                            }else if (mrtCount == 2||mrtCount == 5||mrtCount == 8||mrtCount == 11){
                                mrtCost = mrtCost + 3;
                            }else if (mrtCount > 11 && mrtCount < 18 ){
                                mrtCost = 42;
                            }else{
                                mrtCost = mrtCost + 2;
                            }
                            mrtCount++;

                            break;
                        case "ARL":
                            if (arlCount <= 1){
                                arlCost = stationList.get(j).getPrice();
                            }else{
                                arlCost = arlCost + 5;
                            }
                            arlCount++;
                            break;
                    }
                }
            }
            // เอาการแสดงผลไปใส่ในรายการอีกทีหนึ่ง
            direction_collection.add(direction);
        }
        //เก็บเพิ่มค่าใช้จ่ายไว้ใน sum
        btsCsum += btsCost;
        brtCsum += brtCost;
        mrtCsum += mrtCost;
        arlCsum += arlCost;
        //รวมผลค่าใช้จ่ายไว้ใน cost
        cost = btsCsum+brtCsum+mrtCsum+arlCsum+btsExtd;
        //แสดงผลบน TextBox ด้านบน
        headList.setText(arrayPath.get(lastIndex)+""+
                "\nCost = "+cost+" THB"+
                "\nBTS = "+btsCsum+" THB"+
                "\nBRT = "+brtCsum+" THB"+
                "\nMRT = "+mrtCsum+" THB"+
                "\nARL = "+arlCsum+" THB"+
                "\nBTSEXTEND = "+btsExtd+" THB");
        // setupList เพื่อที่จะแสดงผลโดยใช้ข้อมูลจากรายการ direction_collection

        ListAdapter adapter = new SimpleAdapter(DirectionsListview.this, direction_collection,
                R.layout.direction_row, new String[] {IMAGE,STATUS,STATION}, new int[] {R.id.list_icon, R.id.status,R.id.stations});
        // แสดงผลตามนี้
        listView.setAdapter(adapter);
    }
}

