package com.artem.weather;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

public class Options extends AppCompatActivity{

    RadioGroup time;
    RadioGroup precipitation;
    RadioGroup temperature;
    RadioGroup wind;

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar toolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(toolbar);

        prefs = this.getSharedPreferences("weather", MODE_PRIVATE);
        final boolean timeFormat12Hr= prefs.getBoolean("12HourFormat", true);
        final boolean precipitationMM = prefs.getBoolean("precipitationMM", true);
        final boolean tempInCelcius = prefs.getBoolean("temperatureCelcius", true);
        final boolean windInMPH = prefs.getBoolean("windMPH", true);

        prefsEditor = getPreferences(MODE_PRIVATE).edit();

        time = (RadioGroup) findViewById(R.id.time_radio_group);
        time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //Changes the format of the time being displayed
                switch(id)
                {
                    case R.id.format_12hour_button:
                        //switch to 12hour format
                        if(!timeFormat12Hr) {
                            prefsEditor.putBoolean("12HourFormat", true);
                            prefsEditor.apply();
                        }
                        break;

                    case R.id.format_24hour_button:
                        //switch to 24hour format
                        if(timeFormat12Hr) {
                            prefsEditor.putBoolean("12HourFormat", false);
                            prefsEditor.apply();
                        }
                        break;

                }
            }
        });

        precipitation = (RadioGroup) findViewById(R.id.precipitation_radio_group);
        precipitation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //Changes precipitation format to inches or mm
                switch(id)
                {
                    case R.id.mm_button:
                        //Switch to milimetres
                        if(!precipitationMM)
                        {
                            prefsEditor.putBoolean("precipitationMM", true);
                            prefsEditor.apply();
                        }
                        break;

                    case R.id.inches_button:
                        //Switch to inches
                        if(precipitationMM)
                        {
                            prefsEditor.putBoolean("precipitationMM", false);
                            prefsEditor.apply();
                        }
                        break;
                }
            }
        });

        temperature = (RadioGroup) findViewById(R.id.temperature_radio_group);
        temperature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //Changes format of the temperature being shown in Fahrenheit or Celcius
                switch(id)
                {
                    case R.id.celcius_button:
                        //Switch to Celcius
                        if(!tempInCelcius)
                        {
                            prefsEditor.putBoolean("temperatureCelcius", true);
                            prefsEditor.apply();
                        }
                        break;

                    case R.id.fahrenheit_button:
                        //Switch to Fahrenheit
                        if(tempInCelcius)
                        {
                            prefsEditor.putBoolean("temperatureCelcius", false);
                            prefsEditor.apply();
                        }
                        break;
                }

            }
        });

        wind = (RadioGroup) findViewById(R.id.wind_radio_group);
        wind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //Changes the wind speed to MPH or KPH
                switch(id)
                {
                    case R.id.mph_button:
                        //Switch to MPH
                        if(!windInMPH)
                        {
                            prefsEditor.putBoolean("windMPH", true);
                            prefsEditor.apply();
                        }
                        break;
                    case R.id.km_button:
                        //switch to KPH
                        if(windInMPH)
                        {
                            prefsEditor.putBoolean("windMPH", false);
                            prefsEditor.apply();
                        }
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.back_button:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
