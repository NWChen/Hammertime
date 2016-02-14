package com.example.nl.hammertime;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TimePicker;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SetAlarm extends AppCompatActivity implements View.OnClickListener {

    final String requestURL = "http://160.39.166.246:8000/alarm_time";
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    TimePicker tpAlarmPicker;
    ImageButton ibAlarmBack;
    Button bConfirmAlarm;
    TextClock tcInvClock;

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
        tcInvClock = (TextClock) findViewById(R.id.tcInvClock);

        bConfirmAlarm.setOnClickListener(this);
        ibAlarmBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bConfirmAlarm:

                boolean bPM = tcInvClock.getText().toString().substring(9,11).equals("PM") ? true : false;
                String currentHourS = tcInvClock.getText().toString().substring(0, 2);
                int currentHour = Integer.parseInt(currentHourS);
                if (bPM)
                    currentHour += 12;
                String currentMinuteS = tcInvClock.getText().toString().substring(3, 5);
                int currentMinute = Integer.parseInt(currentMinuteS);
                String currentSecS = tcInvClock.getText().toString().substring(6, 8);
                int currentSec = Integer.parseInt(currentSecS);
                String currentTime = currentHour + ":" + currentMinute + ":" + currentSec;
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hourChosen = tpAlarmPicker.getHour();
                    minuteChosen = tpAlarmPicker.getMinute();
                } else {
                    hourChosen = tpAlarmPicker.getCurrentHour();
                    minuteChosen = tpAlarmPicker.getCurrentMinute();
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url;
                HttpURLConnection conn;
                try {
                    //Set up connection
                    url = new URL(requestURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Content-Type","application/json");
                    conn.connect();

                    //Set up JSON object
                    JSONObject jsonParam = new JSONObject();

                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    currentTime = calendar.getTime().toString();
                    calendar.set(Calendar.HOUR, hourChosen);
                    calendar.set(Calendar.MINUTE, minuteChosen);
                    calendar.set(Calendar.SECOND, 0);
                    String alarmTime = calendar.getTime().toString();

                    jsonParam.put("current_time", currentTime);
                    jsonParam.put("alarm_time", alarmTime);
                    //jsonParam.put("alarm_time", hourChosen + ":" + minuteChosen + ":00");

                    //Set up output stream in byte data
                    DataOutputStream output;
                    String str = jsonParam.toString();
                    byte[] data = str.getBytes("UTF-8");

                    //Write output and close connections.
                    output = new DataOutputStream(conn.getOutputStream());
                    output.write(data);
                    output.flush();
                    output.close();
                    conn.getResponseCode();
                    conn.disconnect();

                    Snackbar.make(v, "Alarm Confirmed", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } catch(Exception e) {
                    Snackbar.make(v, "Error Occurred", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    e.printStackTrace();
                } finally {

                }
                break;
            case R.id.ibAlarmBack:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
