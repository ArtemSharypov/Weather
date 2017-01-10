package com.artem.weather;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar toolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(toolbar);

        time = (RadioGroup) findViewById(R.id.time_radio_group);
        time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id)
                {
                    case R.id.format_12hour_button:
                        //change to 12hour format
                        break;

                    case R.id.format_24hour_button:
                        //change to 24 hour format
                        break;

                }
            }
        });

        precipitation = (RadioGroup) findViewById(R.id.precipitation_radio_group);
        precipitation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id)
                {
                    case R.id.mm_button:
                        //change precipitation to mm
                        break;

                    case R.id.inches_button:
                        //change precipitation to in
                        break;
                }
            }
        });

        temperature = (RadioGroup) findViewById(R.id.temperature_radio_group);
        temperature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id)
                {
                    case R.id.celcius_button:
                        //change to celcius
                        break;

                    case R.id.fahrenheit_button:
                        //change to fahrenheit
                        break;
                }

            }
        });

        wind = (RadioGroup) findViewById(R.id.wind_radio_group);
        wind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id)
                {
                    case R.id.mph_button:
                        //switch to mph
                        break;
                    case R.id.km_button:
                        //switch to km
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
                //dialog button maybe
                finish();
                break;
            case R.id.save_button:
                //update shared preferences
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
