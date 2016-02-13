package com.example.nl.hammertime;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

public class SetActive extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    SensorManager sensorManager;
    Sensor light;
    ImageButton ibActiveBack;
    TextView tvLight;
    TextClock tcClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_active);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ibActiveBack = (ImageButton) findViewById(R.id.ibActiveBack);
        tvLight = (TextView) findViewById(R.id.tvLight);
        tcClock = (TextClock) findViewById(R.id.tcClock);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


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
        float ill = event.values[0];
        boolean bPM = tcClock.getText().toString().substring(9,11).equals("PM") ? true : false;
        String currentHourS = tcClock.getText().toString().substring(0, 2);
        int currentHour = Integer.parseInt(currentHourS);
        if (currentHourS.charAt(0) == '0')
            currentHour = Integer.parseInt(currentHourS.substring(1,2));
        if (bPM)
            currentHour += 12;

        String currentMinuteS = tcClock.getText().toString().substring(3, 5);
        int currentMinute = Integer.parseInt(currentMinuteS);
        if (currentMinuteS.charAt(0) == '0')
            currentMinute = Integer.parseInt(currentMinuteS.substring(1,2));

        if (currentHour > SetAlarm.hourChosen)
            tvLight.setText("YES");
        else if (currentHour == SetAlarm.hourChosen && currentMinute >= SetAlarm.minuteChosen)
            tvLight.setText("YES");
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

    }
}
