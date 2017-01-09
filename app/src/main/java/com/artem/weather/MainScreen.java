package com.artem.weather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity{

    //Queries WunderGround API for weather
    private class WGQuerier extends AsyncTask<String, Void, String>
    {
        private String city;
        private String country;
        private String queryType;
        private long latitude;
        private long longitude;
        private final String INITIAL_CALL
                = "http://api.wunderground.com/api/" + APIHolder.WG_API_KEY + "/";

        public WGQuerier(String city, String country, String queryType)
        {
            this.city = city;
            this.country = country;
            this.queryType = queryType;
        }

        //Longitude/latitude need a initial geolookup call THEN can do the normal call
        public WGQuerier(long latitude, long longitude, String queryType)
        {
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
                        case CONDITIONS:
                            urlInfo += "conditions/q/";
                            break;
                        case FORECAST:
                            urlInfo += "forecast/q/";
                            break;
                        case FORECAST_10DAY:
                            urlInfo += "forecast10day/q/";
                            break;
                        case HOURLY:
                            urlInfo += "hourly/q/";
                            break;
                        case HOURLY_10DAY:
                            urlInfo += "hourly10day/q/";
                            break;
                        case AUTO_COMPLETE:
                            urlInfo = "http://autocomplete.wunderground.com/aq?query=" + city;
                            break;
                    }

                    //need to find a better place or way to do it without code repetition
                    if(!queryType.equals(AUTO_COMPLETE))
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
            if(queryType.equals(CONDITIONS))
            {
                populateInfo(data);
            }
            else
            {
                populateDailyHourly(data, queryType);
            }
        }
    }

    //Used to query Google Place API for autocomplete locations
    private class GooglePlaceQuerier extends AsyncTask<String, Void, String>
    {
        private final String INITIAL_CALL ="https://maps.googleapis.com/maps/api/place";
        private final String TYPE_AUTO_COMPLETE = "/autocomplete";
        private final String FILE_TYPE = "/json";

        private String input;

        public GooglePlaceQuerier(String input)
        {
            this.input = input;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String urlInfo;
            String locationData = "";

            try
            {
                //Can't download without any internet connection
                if(isNetworkAvailable())
                {
                    urlInfo =  INITIAL_CALL + TYPE_AUTO_COMPLETE + FILE_TYPE;
                    urlInfo += "?key=" + APIHolder.GOOGLE_API_KEY;
                    urlInfo += "&types=(cities)"; //need a way to better filter for cities / country
                    urlInfo += "&input=" + URLEncoder.encode(input, "utf8");

                    url = new URL(urlInfo);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer jsonString = new StringBuffer();
                    String temp = "";

                    //Read in everything
                    while ((temp = reader.readLine()) != null) {
                        jsonString.append(temp).append("\n");
                    }

                    locationData = jsonString.toString();
                    reader.close();
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

            return locationData;
        }

        @Override
        protected void onPostExecute(String autoCompleteJSON) {
            super.onPostExecute(autoCompleteJSON);
            //autoCompleteAdapter.getFilter().filter(autoCompleteJSON);
            System.out.println(autoCompleteJSON);
        }
    }

    private RecyclerView recyclerView;
    private TextView location;
    private TextView temperature;
    private TextView feelsLike;
    private TextView windInfo;
    private TextView precipAmt;
    private TextView precipChance;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private TextView dateLastUpdated;
    private TextView currConditions;
    private ImageView weatherIcon;
    private Toolbar toolbar;
    private Button forecast3Day;
    private Button forecast10Day;
    private Button hourly48Hours;
    private Button hourly10Days;
    private AutoCompleteTextView autoCompleteText;

    private final int FORECAST_3DAY_MODE = 1;
    private final int FORECAST_10DAY_MODE = 2;
    private final int HOURLY_2DAY_MODE = 3;
    private final int HOURLY_10DAY_MODE = 4;

    private final String CONDITIONS = "conditions";
    private final String FORECAST = "forecast";
    private final String FORECAST_10DAY = "forecast10day";
    private final String HOURLY = "hourly";
    private final String HOURLY_10DAY = "hourly10day";

    private final String AUTO_COMPLETE = "autocomplete";

    private AutoCompleteArrayAdapter autoCompleteAdapter;
    private WeatherAdapter adapter;
    private ArrayList<WeatherInfo> weatherList;
    private ArrayList<String> locationList;
    private WeatherInfo currentWeather;

    private String currCity = "Winnipeg";
    private String currCountry = "Canada";

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

        //Changes mode based on the current display in the forecasts
        forecast3Day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(current_mode != FORECAST_3DAY_MODE)
                {
                    new WGQuerier(currCity, currCountry, FORECAST).execute();
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
                    new WGQuerier(currCity, currCountry, FORECAST_10DAY).execute();
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
                    new WGQuerier(currCity, currCountry, HOURLY).execute();
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
                    new WGQuerier(currCity, currCountry, HOURLY_10DAY).execute();
                    current_mode = HOURLY_10DAY_MODE;
                }
            }
        });

        defaultDisplay();
    }

    //Creates the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        View view = menu.findItem(R.id.search_auto).getActionView();

        autoCompleteText = (AutoCompleteTextView) view.findViewById(R.id.search);
        autoCompleteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new GooglePlaceQuerier(charSequence.toString()).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteAdapter = new AutoCompleteArrayAdapter(this, android.R.layout.simple_list_item_1, locationList);
        autoCompleteText.setAdapter(autoCompleteAdapter);

        //Sets the texts in it, allowing the search
        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = (String) parent.getItemAtPosition(position);
                autoCompleteText.setText(location);

            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    //Handles clicks for the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.search_button:

                break;
            case R.id.options_button:
                Intent intent = new Intent(this, Options.class);
                startActivity(intent);
                break;
        }

        return result;
    }

    //Displays the current conditions & forecast
    private void defaultDisplay()
    {
        //will change depending on the city, just a default location for now.
        //second query changes based on preferences, forecast will be default
        new WGQuerier(currCity, currCountry, CONDITIONS).execute();
        new WGQuerier(currCity, currCountry, FORECAST).execute(); //will change depending on preferences / options
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

                temperature.setText(currentWeather.getTemperature());
                feelsLike.setText(currentWeather.getFeelsLike());

                windInfo.setText(currentWeather.getWindSpeed() + currentWeather.getWindDirection() +
                        "\n" + currentWeather.getWindGust());

                precipAmt.setText(currentWeather.getPrecipitationAmount());
                humidity.setText(currentWeather.getHumidity());

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

    private void populateDailyHourly(String weatherJSON, String parseType)
    {
        try
        {
            JSONObject weather = new JSONObject(weatherJSON);

            if(weather != null)
            {
                JSONParser parser = new JSONParser(this);

                //Checks what its parsing for
                if(parseType.equals(FORECAST) || parseType.equals(FORECAST_10DAY))
                {
                    ArrayList<WeatherInfo> newWeatherInfo = parser.parseDaily(weather);
                    adapter.swap(newWeatherInfo);
                }
                else if(parseType.equals(HOURLY) || parseType.equals(HOURLY_10DAY))
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
}
