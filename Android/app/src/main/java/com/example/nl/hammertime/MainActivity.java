package com.example.nl.hammertime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bSetAlarm, bSetActive, bChooseSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button bSetAlarm = (Button) findViewById(R.id.bSetAlarm);
        Button bSetActive = (Button) findViewById(R.id.bSetActive);
        Button bSettings = (Button) findViewById(R.id.bSettings);

        bSetAlarm.setOnClickListener(this);
        bSetActive.setOnClickListener(this);
        bSettings.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bSetAlarm:
                startActivity(new Intent(this, SetAlarm.class));
                break;
            case R.id.bSetActive:
                startActivity(new Intent(this, SetActive.class));
                break;
            case R.id.bSettings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
    }
}
