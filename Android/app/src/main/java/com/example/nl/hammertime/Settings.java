package com.example.nl.hammertime;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    /*
    DetectChoice
    0 - None
    1 - Accelerometer
    2 - Light
    3 - Camera
     */
    ImageButton ibSettingsBack;
    RadioButton rbAccelerometer, rbLight, rbCamera;
    public static int detectChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ibSettingsBack = (ImageButton) findViewById(R.id.ibSettingsBack);
        rbAccelerometer = (RadioButton) findViewById(R.id.rbAccelerometer);
        rbLight = (RadioButton) findViewById(R.id.rbLight);
        rbCamera = (RadioButton) findViewById(R.id.rbCamera);

        ibSettingsBack.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ibSettingsBack:
                detectChoice = rbAccelerometer.isChecked() ? 1 : detectChoice;
                detectChoice = rbLight.isChecked() ? 2 : detectChoice;
                detectChoice = rbCamera.isChecked() ? 3 : detectChoice;
                startActivity(new Intent(this, MainActivity.class));
        }
    }
}
