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

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class SetActive extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    final String requestURL = "http://160.39.166.246:8000/alarm_time";
    final float LIGHT_THRESHHOLD = 15;
    SensorManager sensorManager;
    Sensor light;
    ImageButton ibActiveBack;
    TextView tvLight,tvAlarmTime;
    TextClock tcClock;
    private String cameraId;
    boolean currBool, prevBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_active);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ibActiveBack = (ImageButton) findViewById(R.id.ibActiveBack);
        tvLight = (TextView) findViewById(R.id.tvLight);
        tcClock = (TextClock) findViewById(R.id.tcClock);
        tvAlarmTime = (TextView) findViewById(R.id.tvAlarmTime);

        boolean isAM = SetAlarm.hourChosen > 12 ? false : true;
        String alarmHours = SetAlarm.hourChosen > 12 ? Integer.toString(SetAlarm.hourChosen - 12 ): Integer.toString(SetAlarm.hourChosen);
        alarmHours = alarmHours.substring(1,2).equals(":") ? "0" + alarmHours : alarmHours;
        String alarmMinutes = Integer.toString(SetAlarm.minuteChosen);
        alarmMinutes = alarmMinutes.substring(1,2).equals(":") ? "0" + alarmMinutes : alarmMinutes;
        String alarmTime = isAM ? alarmHours + ":" + alarmMinutes + ":00 AM" : alarmHours + ":" + alarmMinutes + ":00 PM";
        tvAlarmTime.setText(alarmTime);



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
/*
    private void setupCamera2() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_FRONT)
                    continue;
                this.cameraId = cameraId;
                int[] picSize = Settings.getPictureSize();
            }
        }
  }*/

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
        float ill = event.values[0];

        boolean bPM = tcClock.getText().toString().substring(9,11).equals("PM") ? true : false;
        String currentHourS = tcClock.getText().toString().substring(0, 2);
        int currentHour = Integer.parseInt(currentHourS);
        //if (currentHourS.charAt(0) == '0')
            //currentHour = Integer.parseInt(currentHourS.substring(1,2));
        if (bPM)
            currentHour += 12;
        String currentMinuteS = tcClock.getText().toString().substring(3, 5);
        int currentMinute = Integer.parseInt(currentMinuteS);
        //if (currentMinuteS.charAt(0) == '0')
            //currentMinute = Integer.parseInt(currentMinuteS.substring(1,2));
        if (currentHour > SetAlarm.hourChosen || (currentHour == SetAlarm.hourChosen && currentMinute >= SetAlarm.minuteChosen)) {
            tvLight.setText("YES");

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
                currBool = ill > LIGHT_THRESHHOLD ? true : false;
                if (currBool != prevBool) {
                    if (ill > LIGHT_THRESHHOLD)
                        jsonParam.put("isAwake", true);
                    else
                        jsonParam.put("isAwake", false);
                }
                prevBool = currBool;
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
        else
            tvLight.setText("NO");
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //If accuracy changes
    }
}