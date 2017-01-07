package com.artem.weather;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

//might switch to context only constructor, each method just takes a String representing the JSONObject to parse, might be easier
public class JSONParser {

    private JSONObject weatherToParse;
    private JSONObject weatherDisplayLoc; //display_location
    private JSONObject weatherDataSection;
    private Context context;

    public JSONParser(Context context, JSONObject weatherData)
    {
        this.weatherToParse = weatherData;
        this.context = context;
    }

    //sets the section of data to parse next
    private void splitData()
    {
        try
        {
            weatherDataSection = weatherToParse.getJSONObject("current_observation");
            weatherDisplayLoc = weatherDataSection.getJSONObject("display_location");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    //need to fix it still for forecast / hourly and such, only for current_conditions currently
    //Parses all info needed into a weather object and returns it
    public WeatherInfo parseForWeather()
    {
        WeatherInfo weather = new WeatherInfo(context);
        String info = "";

        splitData();

        try
        {
            info = weatherDisplayLoc.getString("full");
            weather.setLocation(info);

            info = weatherDataSection.getString("observation_time");
            weather.setDateUpdated(info);

            info = weatherDataSection.getString("temp_c");
            weather.setTemperature(info);

            info = weatherDataSection.getString("relative_humidity");
            weather.setHumidity(info);

            info = weatherDataSection.getString("wind_dir");
            weather.setWindDirection(info);

            info = weatherDataSection.getString("wind_mph");
            weather.setWindSpeed(info);

            info = weatherDataSection.getString("wind_gust_mph");
            weather.setWindGust(info);

            info = weatherDataSection.getString("feelslike_c");
            weather.setFeelsLike(info);

            info = weatherDataSection.getString("icon");
            weather.setIconToUse(info);

            info = weatherDataSection.getString("precip_today_in");
            weather.setPrecipitationAmount(info);

            //no reliable way to get precip % on just current conditions
            //sunrise and sunset
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return weather;
    }

    //all will create a WeatherInfo object that gets returned with either a string city name or location
    //parse conditions

    //for forecast & 10days just need a private method that'll parse a single period / day object and return it
    //should be a ton easier
    //parse forecast

    //parse forecast 10days

    //for hourly could just make another private method that parses the hour for the information needed and return it
    //should work for any hourly conditions
    //parse hourly

    //parse hourly 10days (only using 48 hours though)

}
