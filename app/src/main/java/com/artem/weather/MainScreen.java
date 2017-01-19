package com.artem.weather;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

public class MainScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    //Queries WunderGround API for weather
    private class WGQuerier extends AsyncTask<String, Void, String>
    {
        private String city;
        private String country;
        private String queryType;
        private double latitude;
        private double longitude;
        private final String INITIAL_CALL
                = "http://api.wunderground.com/api/" + APIHolder.WG_API_KEY + "/";

        //Used for finding the current weather in a city, country
        public WGQuerier(String city, String country, String queryType)
        {
            this.city = city;
            this.country = country;
            this.queryType = queryType;
        }

        //Used for finding the city, country of a GPS location
        public WGQuerier(double latitude, double longitude, String queryType)
        {
            this.latitude = latitude;
            this.longitude = longitude;
            this.queryType = queryType;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            String weatherData = "";
            URL url;
            String urlInfo = INITIAL_CALL;

            //Tries to connect to the API and get the conditions for that city or find the city
            //based on location
            try
            {
                //Can't download without any internet connection
                if(isNetworkAvailable())
                {
                    //Changes the URL based on the query requested
                    switch(queryType)
                    {
                        case CONDITIONS:
                            urlInfo += "conditions";
                            break;
                        case FORECAST:
                            urlInfo += "forecast";
                            break;
                        case FORECAST_10DAY:
                            urlInfo += "forecast10day";
                            break;
                        case HOURLY:
                            urlInfo += "hourly";
                            break;
                        case HOURLY_10DAY:
                            urlInfo += "hourly10day";
                            break;
                        case ASTRONOMY:
                            urlInfo += "astronomy";
                            break;
                        case GEOLOOKUP:
                            urlInfo += "geolookup";
                            break;
                    }

                    //Changes URL and necessary info depending on query type
                    if(queryType.equals(GEOLOOKUP))
                        urlInfo += "/q/" + latitude + "," + longitude + ".json";
                    else
                        urlInfo += "/q/" + country + "/" + city + ".json";

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
            if(queryType.equals(ASTRONOMY))
                populateSunData(data);
            else if(queryType.equals(CONDITIONS))
                populateInfo(data);
            else if(queryType.equals(GEOLOOKUP)) //finds the location before data population
                handleCoordinates(data);
            else
                populateDailyHourly(data, queryType);
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
            autoCompleteAdapter.getFilter().filter(autoCompleteJSON);
        }
    }

    private RecyclerView recyclerView;
    private TextView location;
    private TextView temperature;
    private TextView feelsLike;
    private TextView windInfo;
    private TextView precipAmt;
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

    //Query Types / Modes
    private final String CONDITIONS = "conditions";
    private final String FORECAST = "forecast";
    private final String FORECAST_10DAY = "forecast10day";
    private final String HOURLY = "hourly";
    private final String HOURLY_10DAY = "hourly10day";
    private final String ASTRONOMY = "astronomy";
    private final String GEOLOOKUP = "geolookup";

    private String current_mode; //Data thats displayed in the recyclerview

    private AutoCompleteArrayAdapter autoCompleteAdapter;
    private WeatherAdapter adapter;
    private ArrayList<WeatherInfo> weatherList;
    private ArrayList<String> locationList;
    private WeatherInfo currentWeather;

    private String currCity = "Winnipeg";
    private String currCountry = "Canada";
    private Location lastLocation;

    private GoogleApiClient googleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

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

        current_mode = FORECAST; //keep track of the current mode

        //Changes mode based on the current display in the forecasts
        forecast3Day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(!current_mode.equals(FORECAST))
                {
                    new WGQuerier(currCity, currCountry, FORECAST).execute();
                    current_mode = FORECAST;
                }
            }
        });

        forecast10Day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(!current_mode.equals(FORECAST_10DAY))
                {
                    new WGQuerier(currCity, currCountry, FORECAST_10DAY).execute();
                    current_mode = FORECAST_10DAY;
                }
            }
        });

        hourly48Hours.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(!current_mode.equals(HOURLY))
                {
                    new WGQuerier(currCity, currCountry, HOURLY).execute();
                    current_mode = HOURLY;
                }
            }
        });

        hourly10Days.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(!current_mode.equals(HOURLY_10DAY))
                {
                    new WGQuerier(currCity, currCountry, HOURLY_10DAY).execute();
                    current_mode = HOURLY_10DAY;
                }
            }
        });

        populateDisplays();

        //Checks if the device has google play services / it works before building
        if(checkPlayServices())
            buildGoogleApiClient();
    }

    //Tries to find the current location
    private void findLocation()
    {
        try
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        catch(SecurityException error)
        {
            Toast.makeText(getApplicationContext(),
                    "Couldn't find the location, make sure GPS is enabled", Toast.LENGTH_LONG)
                    .show();
        }

        //Checks if there is a location that can be queried
        if(lastLocation != null)
        {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            new WGQuerier(latitude, longitude, GEOLOOKUP).execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Couldn't find the location, make sure GPS is enabled", Toast.LENGTH_LONG)
                    .show();
        }
    }

    //Builds the API client for allowing locations
    private synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }

    //Makes sure play services are enabled on the device
    private boolean checkPlayServices()
    {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        boolean works = true;

        //Checks that it worked
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GoogleApiAvailability.getInstance().isUserResolvableError(resultCode))
            {
                GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else //Not a supported device
            {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            works = false;
        }

        return works;
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

        //Checks what was clicked
        switch(item.getItemId())
        {
            case R.id.search_button: //Queries for the city entered
                String searchItem = autoCompleteText.getText().toString();
                String[] splitItems = searchItem.split(",");

                currCity = splitItems[0].trim();

                //checks how many words there are, normally city, country
                if(splitItems.length == 2)
                {
                    currCountry = splitItems[1].trim();
                    populateDisplays();
                }
                else if(splitItems.length == 3)
                {
                    currCountry = splitItems[2].trim();
                    populateDisplays();
                }

                location.setText(currCity + currCountry);

                break;
            case R.id.options_button: //Switches to Options layout
                Intent intent = new Intent(this, Options.class);
                startActivity(intent);
                break;
            case R.id.gps_button:
                findLocation();
        }

        return result;
    }

    //Queries the data for forecast, curr conditions and astronomy info
    private void populateDisplays()
    {
        //second query changes based on preferences, forecast will be default
        new WGQuerier(currCity, currCountry, CONDITIONS).execute();
        new WGQuerier(currCity, currCountry, FORECAST).execute();
        new WGQuerier(currCity, currCountry, ASTRONOMY).execute();
    }

    //checks if the phones connected to the internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Sets the sunrise and sunset time for that city
    private void populateSunData(String astronomyJSON)
    {
        try {
            JSONObject astronomyData = new JSONObject(astronomyJSON);

            if(astronomyData != null)
            {
                JSONParser parser = new JSONParser(this);
                ArrayList<String> sunData = parser.parseAstronomy(astronomyData);

                if(sunData.size() > 0)
                    sunrise.setText(sunData.get(0));

                if(sunData.size() > 1)
                    sunset.setText(sunData.get(1));
            }
        }
        catch (JSONException error)
        {
            error.printStackTrace();
        }
    }

    //Populates the main textfields / information
    private void populateInfo(String weatherJSON)
    {
        try {
            JSONObject weather = new JSONObject(weatherJSON);

            //Fills in all of the necessary info
            if(weather != null) {
                JSONParser parser = new JSONParser(this);
                currentWeather = parser.parseConditions(weather);

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

    //Changes whats currently displayed in the RecyclerView for upcoming weather
    //Calls the parser with the according method to read the data and then swaps the adapter
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

    //Tries to get the city and country from the location JSON
    //then calls to populate the information
    private void handleCoordinates(String locationJSON)
    {
        try
        {
            JSONObject locationName = new JSONObject(locationJSON);
            JSONObject location = locationName.getJSONObject("location");

            String tempCountry = location.getString("country_name");

            //USA uses country based on state, not the country itself
            if(tempCountry.equals("USA"))
            {
                currCountry = location.getString("state");
                currCity = location.getString("city");
            }
            else
            {
                currCountry = tempCountry;

                //Wunderground handles Canada weird, parts of the city are called city.
                //So it splits tz_long which is America/City in canada
                String tempCity = location.getString("tz_long");
                String[] split = tempCity.split("/");

                currCity = split[split.length - 1]; //City should be at the end
            }
            //Replaces spaces with underscores to make the query still work
            currCity = currCity.replaceAll(" ", "_");

            populateDisplays();
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Tries to get the last location
        try
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        catch(SecurityException error)
        {
            Toast.makeText(getApplicationContext(),
                    "Couldn't find the location, make sure GPS is enabled", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                "Failed to connect", Toast.LENGTH_LONG)
                .show();
    }
}
