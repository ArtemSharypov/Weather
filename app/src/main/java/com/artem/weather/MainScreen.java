package com.artem.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    ArrayList<WeatherInfo> weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.forecast);

        //Allows the RecyclerView to be horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        weatherList = new ArrayList<WeatherInfo>(); //just a temp array, going to have to call the parser later
        WeatherAdapter adapter = new WeatherAdapter(this, weatherList);

        //Fills the view with items from weatherList
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Creates the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Handles clicks for the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = super.onOptionsItemSelected(item);

        return result;
    }

    //on click for buttons, buttons change the content in the recyclerView
}
