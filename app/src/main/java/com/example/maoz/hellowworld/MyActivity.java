package com.example.maoz.hellowworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends navigation_drawer{

    Button show_msg,show_map;
    EditText get_text;//Declare
    TextView text_view,tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ประกาศทุกหน้าที่เป็น หน้าลูก
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_my, null, false);
        drawerLayout.addView(contentView, 0);
        //

        get_text = (EditText) findViewById(R.id.edit_Text); //set xml to Declare
        show_msg = (Button) findViewById(R.id.btn_msg);
        show_map = (Button) findViewById(R.id.btn_map);
        text_view = (TextView) findViewById(R.id.text_View);
        tv = (TextView) findViewById(R.id.showLocation);
        show_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), get_text.getText(), Toast.LENGTH_SHORT).show();
                text_view.setText(get_text.getText()); //textview to textfield

            }
        });
        linked();

    }
    public void linked(){
        LinkedList list = new LinkedList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        Log.d("println","lList - print linkedlist: " + list);
        Log.d("println","lList.size() - print linkedlist size: " + list.size());
        Log.d("println","lList.get(3) - get 3rd element: " + list.getAt(3));
        Log.d("println","lList.remove(2) - remove 2nd element: " + list.removeAt(2));
        Log.d("println","lList.get(3) - get 3rd element: " + list.getAt(3));
        Log.d("println","lList.size() - print linkedlist size: " + list.size());
        Log.d("println","lList - print linkedlist: " + list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
