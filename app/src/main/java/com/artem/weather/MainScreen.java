package com.artem.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    private class DataQuerier extends AsyncTask<String, Void, String>
    {
        private String location;
        private String queryType;
        private long latitude;
        private long longitude;
        private final String INITIAL_CALL;

        public DataQuerier(String location, String queryType)
        {
            INITIAL_CALL = "http://api.wunderground.com/api/" + APIHolder.API_KEY;
            this.location = location;
            this.queryType = queryType;
        }

        //Longitude/latitude need a intial geolookup call THEN can do the normal call
        public DataQuerier(long latitude, long longitude, String queryType)
        {
            INITIAL_CALL = "http://api.wunderground.com/api/" + APIHolder.API_KEY + "/";
            this.latitude = latitude;
            this.longitude = longitude;
            this.queryType = queryType;
        }

        //Downloads the JSONObject file in the background
        @Override
        protected String doInBackground(String... strings)
        {
            String weatherData = "";
            URL url;
            String urlInfo = INITIAL_CALL;

            //Tries to connect to the API and get the conditions for that city
            try
            {
                //Can't download without any internet connection
                if(isNetworkAvailable())
                {
                    //Changes the URL based on the query requested
                    switch(queryType)
                    {
                        case "conditions":
                            urlInfo += "conditions/q" + location + ".json";
                            break;
                        case "forecast":
                            urlInfo += "forecast/q/" + location + ".json";
                            break;
                        case "forecast10day":
                            urlInfo += "forecast/q/" + location + ".json";
                            break;
                        case "hourly":
                            urlInfo += "hourly/q/" + location + ".json";
                            break;
                        case "hourly10day":
                            urlInfo += "forecast/q/" + location + ".json";
                            break;
                        case "autocomplete":
                            urlInfo = "http://autocomplete.wunderground.com/aq?query=" + location + ".json";
                            break;
                    }

                    url = new URL(urlInfo);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer jsonString = new StringBuffer();
                    String temp = "";

                    //Read in everything
                    while ((temp = reader.readLine()) != null) {
                        jsonString.append(temp).append("\n");
                    }

                    reader.close();
                    weatherData = weatherData + jsonString.toString();
                }
            }
            catch(MalformedURLException error)
            {
                error.printStackTrace();
            }
            catch(IOException error)
            {
                error.printStackTrace();
            }

            return weatherData;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            weatherJSON = data;
            populateInfo();
        }
    }

    RecyclerView recyclerView;
    TextView location;
    TextView temperature;
    TextView feelsLike;
    TextView windInfo;
    TextView precipAmt;
    TextView precipChance;
    TextView humidity;
    TextView sunrise;
    TextView sunset;
    TextView dateLastUpdated;
    ImageView weatherIcon;

    ArrayList<WeatherInfo> weatherList;
    WeatherInfo currentWeather;
    String weatherJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        recyclerView = (RecyclerView) findViewById(R.id.forecast);

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

        location = (TextView) findViewById(R.id.location);
        temperature = (TextView) findViewById(R.id.current_temp);
        feelsLike = (TextView) findViewById(R.id.feels_like);
        windInfo = (TextView) findViewById(R.id.wind_info);
        precipAmt = (TextView) findViewById(R.id.precipitation_amount);
        precipChance = (TextView) findViewById(R.id.precipitation_chance);
        humidity = (TextView) findViewById(R.id.humidity);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        dateLastUpdated = (TextView) findViewById(R.id.date_update);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);

        new DataQuerier("Winnipeg", "conditions").execute();
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

    //checks if the phones connected to the internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Populates all textfields / text icons
    private void populateInfo()
    {
        try {
            JSONObject weather = new JSONObject(weatherJSON);

            //Fills in all of the necessary info
            if(weather != null) {
                JSONParser parser = new JSONParser(this, weather);
                currentWeather = parser.parseForWeather();

                location.setText(currentWeather.getLocation());
                temperature.setText(currentWeather.getTemperature() + "°C");
                feelsLike.setText(currentWeather.getFeelsLike() + "°C");
                windInfo.setText(currentWeather.getWindSpeed() + "mph " + currentWeather.getWindDirection() +
                        " with gusts of " + currentWeather.getWindGust() + "mph");
                precipAmt.setText(currentWeather.getPrecipitationAmount() + "in");
                precipChance.setText("0%");
                humidity.setText(currentWeather.getHumidity());
                dateLastUpdated.setText(currentWeather.getDateUpdated());
                weatherIcon.setImageBitmap(currentWeather.getIconToUse());
            }
            else
            {
                location.setText("RIP");
            }
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }
    }

    //on click for buttons, buttons change the content in the recyclerView
}
