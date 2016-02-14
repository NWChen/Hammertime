package com.example.nl.hammertime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
//import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.Console;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class SetActive extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    final String requestURL = "http://160.39.166.246:8002/user_awake";
    final float LIGHT_THRESHHOLD = 15;
    final float ACCEL_THRESHHOLDX = 1.0F, ACCEL_THRESHHOLDY = 0,ACCEL_THRESHHOLDZ = 0;
    float threshhold = 0, threshholdx = 0, threshholdy = 0, threshholdz = 0;
    SensorManager sensorManager;
    Sensor sensor;
    ImageButton ibActiveBack;
    TextView tvLight,tvAlarmTime;
    TextClock tcClock;
    private String cameraId;
    boolean currBool, prevBool = false, inTimer = false;
    static int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_active);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        Getting the views from the xml
         */
        ibActiveBack = (ImageButton) findViewById(R.id.ibActiveBack);
        tvLight = (TextView) findViewById(R.id.tvLight);
        tcClock = (TextClock) findViewById(R.id.tcClock);
        tvAlarmTime = (TextView) findViewById(R.id.tvAlarmTime);

        /*
        Set the alarm and current time
         */
        boolean isAM = SetAlarm.hourChosen > 12 ? false : true;
        String alarmHours = SetAlarm.hourChosen > 12 ? Integer.toString(SetAlarm.hourChosen - 12 ): Integer.toString(SetAlarm.hourChosen);
        System.out.println(alarmHours);
        alarmHours = alarmHours.length() == 1 ? "0" + alarmHours : alarmHours;
        //alarmHours = alarmHours.substring(1,2).equals(":") ? "0" + alarmHours : alarmHours;
        String alarmMinutes = Integer.toString(SetAlarm.minuteChosen);
        alarmMinutes = alarmMinutes.length() == 1 ? "0" + alarmMinutes : alarmMinutes;
        //alarmMinutes = alarmMinutes.substring(1,2).equals(":") ? "0" + alarmMinutes : alarmMinutes;
        String alarmTime = isAM ? alarmHours + ":" + alarmMinutes + ":00 AM" : alarmHours + ":" + alarmMinutes + ":00 PM";
        tvAlarmTime.setText(alarmTime);

        /*
        Setting up different sensors based on which one the user chose
         */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (com.example.nl.hammertime.Settings.detectChoice == 1) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            threshholdx = ACCEL_THRESHHOLDX;
            threshholdy = ACCEL_THRESHHOLDY;
            threshholdz = ACCEL_THRESHHOLDZ;
        }

        if (com.example.nl.hammertime.Settings.detectChoice == 2) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            threshhold = LIGHT_THRESHHOLD;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ibActiveBack:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //tvLight.setText("Hi");
        float ill = event.values[0];
        float illy = event.values[1];
        float illz = event.values[2];
        if (inTimer) {
            ill = -1;
            illy = -1;
            illz = -1;
        }
        tvLight.setText(Float.toString(ill) + ", " + Float.toString(illy) + ", " + Float.toString(illz));
        boolean bPM = tcClock.getText().toString().substring(9,11).equals("PM") ? true : false;
        String currentHourS = tcClock.getText().toString().substring(0, 2);
        int currentHour = Integer.parseInt(currentHourS);
        if (bPM)
            currentHour += 12;
        String currentMinuteS = tcClock.getText().toString().substring(3, 5);
        int currentMinute = Integer.parseInt(currentMinuteS);
        if (currentHour > SetAlarm.hourChosen || (currentHour == SetAlarm.hourChosen && currentMinute >= SetAlarm.minuteChosen)) {
            tvLight.setText("YES");
            currBool = ill > threshhold ? true : false;
            if (currBool != prevBool) {

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
                    if (threshhold != 0) {
                        if (ill > threshhold)
                            jsonParam.put("isAwake", true);
                        else
                            jsonParam.put("isAwake", false);
                    } else {

                    }


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
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
            prevBool = currBool;
        }
        //else
            //tvLight.setText("NO");
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //If accuracy changes
    }
}