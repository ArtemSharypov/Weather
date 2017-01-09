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
import android.view.View;
import android.widget.Button;
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
        private String city;
        private String country;
        private String queryType;
        private long latitude;
        private long longitude;
        private final String INITIAL_CALL;

        public DataQuerier(String city, String country, String queryType)
        {
            INITIAL_CALL = "http://api.wunderground.com/api/" + APIHolder.API_KEY + "/";
            this.city = city;
            this.country = country;
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
                            urlInfo += "conditions/q/";
                            break;
                        case "forecast":
                            urlInfo += "forecast/q/";
                            break;
                        case "forecast10day":
                            urlInfo += "forecast10day/q/";
                            break;
                        case "hourly":
                            urlInfo += "hourly/q/";
                            break;
                        case "hourly10day":
                            urlInfo += "hourly10day/q/";
                            break;
                        case "autocomplete":
                            urlInfo = "http://autocomplete.wunderground.com/aq?query=" + city;
                            break;
                    }

                    //need to find a better place or way to do it without code repetition
                    if(!queryType.equals("autocomplete"))
                        urlInfo += country + "/" + city + ".json";

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

            //Check what the query was for and populate that one
            if(queryType.equals("conditions"))
            {
                populateInfo(data);
            }
            else
            {
                populateDailyHourly(data, queryType);
            }
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
    TextView currConditions;
    ImageView weatherIcon;
    Toolbar toolbar;
    Button forecast3Day;
    Button forecast10Day;
    Button hourly48Hours;
    Button hourly10Days;

    WeatherAdapter adapter;
    ArrayList<WeatherInfo> weatherList;
    WeatherInfo currentWeather;

    String currCity = "Winnipeg";
    String currCountry = "Canada";

    private final int FORECAST_3DAY_MODE = 1;
    private final int FORECAST_10DAY_MODE = 2;
    private final int HOURLY_2DAY_MODE = 3;
    private final int HOURLY_10DAY_MODE = 4;
    private int current_mode; //Data thats displayed in the recyclerview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        recyclerView = (RecyclerView) findViewById(R.id.forecast);

        //Allows the RecyclerView to be horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        weatherList = new ArrayList<WeatherInfo>();
        adapter = new WeatherAdapter(this, weatherList);

        //Fills the view with items from weatherList
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        currConditions = (TextView) findViewById(R.id.curr_conditions);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);

        forecast3Day = (Button) findViewById(R.id.forecast_3day);
        forecast10Day = (Button) findViewById(R.id.forcast_10day);
        hourly48Hours = (Button) findViewById(R.id.hourly_2day);
        hourly10Days = (Button) findViewById(R.id.hourly_10day);

        current_mode = FORECAST_3DAY_MODE; //keep track of the current mode

        forecast3Day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(current_mode != FORECAST_3DAY_MODE)
                {
                    new DataQuerier(currCity, currCountry, "forecast").execute();
                    current_mode = FORECAST_3DAY_MODE;
                }
            }
        });

        forecast10Day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(current_mode != FORECAST_10DAY_MODE)
                {
                    new DataQuerier(currCity, currCountry, "forecast10day").execute();
                    current_mode = FORECAST_10DAY_MODE;
                }
            }
        });

        hourly48Hours.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(current_mode != HOURLY_2DAY_MODE)
                {
                    new DataQuerier(currCity, currCountry, "hourly").execute();
                    current_mode = HOURLY_2DAY_MODE;
                }
            }
        });

        hourly10Days.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(current_mode != HOURLY_10DAY_MODE)
                {
                    new DataQuerier(currCity, currCountry, "hourly10day").execute();
                    current_mode = HOURLY_10DAY_MODE;
                }
            }
        });

        //will change depending on the city, just a default location for now.
        //second query changes based on preferences, forecast will be default
        new DataQuerier(currCity, currCountry, "conditions").execute();
        new DataQuerier(currCity, currCountry, "forecast").execute(); //will change depending on preferences / options
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

    //Populates the main textfields / information
    private void populateInfo(String weatherJSON)
    {
        try {
            JSONObject weather = new JSONObject(weatherJSON);

            //Fills in all of the necessary info
            if(weather != null) {
                JSONParser parser = new JSONParser(this);
                currentWeather = parser.parseForWeather(weather);

                location.setText(currentWeather.getLocation());

                temperature.setText("Current Temp: " + currentWeather.getTemperature() + "°C");
                feelsLike.setText("Feels Like: " + currentWeather.getFeelsLike() + "°C");

                windInfo.setText("Wind: " + currentWeather.getWindSpeed() + "mph " +
                        currentWeather.getWindDirection()
                        + "\n Gusts of " + currentWeather.getWindGust() + "mph");

                precipAmt.setText("Snow/Rain:" + currentWeather.getPrecipitationAmount() + "in");
                humidity.setText("Humidity: " + currentWeather.getHumidity());

                dateLastUpdated.setText(currentWeather.getDateUpdated());

                currConditions.setText(currentWeather.getConditions());
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

    //might just split it into 2 methods, have to check it either way
    private void populateDailyHourly(String weatherJSON, String parseType)
    {
        try
        {
            JSONObject weather = new JSONObject(weatherJSON);

            if(weather != null)
            {
                JSONParser parser = new JSONParser(this);

                //Checks what its parsing for
                if(parseType.equals("forecast") || parseType.equals("forecast10day"))
                {
                    ArrayList<WeatherInfo> newWeatherInfo = parser.parseDaily(weather);
                    adapter.swap(newWeatherInfo);
                }
                else if(parseType.equals("hourly") || parseType.equals("hourly10day"))
                {
                    ArrayList<WeatherInfo> newWeatherInfo = parser.parseHourly(weather);
                    adapter.swap(newWeatherInfo);
                }
            }
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }
    }

    //on click for buttons, buttons change the content in the recyclerView
}
