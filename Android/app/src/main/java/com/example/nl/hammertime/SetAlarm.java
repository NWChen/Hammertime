package com.example.nl.hammertime;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;

public class SetAlarm extends AppCompatActivity implements View.OnClickListener {

    TimePicker tpAlarmPicker;
    ImageButton ibAlarmBack;
    Button bConfirmAlarm;

    public static int hourChosen, minuteChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tpAlarmPicker = (TimePicker) findViewById(R.id.tpAlarmPicker);
        ibAlarmBack = (ImageButton) findViewById(R.id.ibAlarmBack);
        bConfirmAlarm = (Button) findViewById(R.id.bConfirmAlarm);

        bConfirmAlarm.setOnClickListener(this);
        ibAlarmBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bConfirmAlarm:
                //hourChosen = tpAlarmPicker.getBaseline();
                //int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                //if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){

                String requestURL = "http://enterURLhere.com";
                URL url;
                HttpURLConnection conn;
                try {
                    url = new URL(requestURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("ID", "25");


                } catch(Exception e) {
                    e.printStackTrace();
                }

                hourChosen = tpAlarmPicker.getHour();
                minuteChosen = tpAlarmPicker.getMinute();

                /*} else {
                    PreparedStatement pstmt;
                    hourChosen = tpAlarmPicker.getCurrentHour();
                    minuteChosen = tpAlarmPicker.getCurrentMinute();
                }*/
                break;
            case R.id.ibAlarmBack:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
}
