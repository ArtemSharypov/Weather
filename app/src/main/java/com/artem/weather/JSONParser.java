package com.artem.weather;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    private Context context;

    public JSONParser(Context context)
    {
        this.context = context;
    }

    //Parses location, date the weather was last updated, temperature, what it feels like out,
    //humidity, precipitation amount, wind speed / direction / gust, and the icon to display and
    //the current conditions
    public WeatherInfo parseForWeather(JSONObject mainData)
    {
        WeatherInfo weather = new WeatherInfo(context);

        try
        {
            JSONObject weatherDataSection = mainData.getJSONObject("current_observation"); //current observations[[[[[[[[[[[[[[[[[[[[[[[]
            JSONObject weatherDisplayLoc = weatherDataSection.getJSONObject("display_location"); //display location

            weather.setLocation(weatherDisplayLoc.getString("full"));
            weather.setDateUpdated(weatherDataSection.getString("observation_time"));

            weather.setTemperature(weatherDataSection.getString("temp_c"));
            weather.setFeelsLike(weatherDataSection.getString("feelslike_c"));

            weather.setHumidity(weatherDataSection.getString("relative_humidity"));
            weather.setPrecipitationAmount(weatherDataSection.getString("precip_today_in"));

            weather.setWindDirection(weatherDataSection.getString("wind_dir"));
            weather.setWindSpeed(weatherDataSection.getString("wind_mph"));
            weather.setWindGust(weatherDataSection.getString("wind_gust_mph"));

            weather.setIconToUse(weatherDataSection.getString("icon"));
            weather.setConditions(weatherDataSection.getString("weather"));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return weather;
    }

    //Parses the JSONObject specified for hourly data
    //Gets temperature, time / day, wind speed and direction, what it feels like, humidity,
    //icon to display, and the conditions outside currently
    public ArrayList<WeatherInfo> parseHourly(JSONObject mainData)
    {
        ArrayList<WeatherInfo> parsedHourlyData = new ArrayList<WeatherInfo>();

        try
        {
            JSONArray hourlyData = mainData.getJSONArray("hourly_forecast");

            //Parses all of the data and adds it to the list
            for(int i = 0; i < hourlyData.length(); i++)
            {
                JSONObject currArrayItem = hourlyData.getJSONObject(i);
                JSONObject time = currArrayItem.getJSONObject("FCTTIME");
                JSONObject temperature = currArrayItem.getJSONObject("temp");
                JSONObject windSpeed = currArrayItem.getJSONObject("wspd");
                JSONObject windDir = currArrayItem.getJSONObject("wdir");
                JSONObject feelsLike = currArrayItem.getJSONObject("feelslike");

                WeatherInfo weather = new WeatherInfo(context);

                weather.setTemperature(temperature.getString("metric"));
                weather.setFeelsLike(feelsLike.getString("metric"));

                weather.setWindSpeed(windSpeed.getString("metric"));
                weather.setWindDirection(windDir.getString("dir"));

                weather.setHumidity(currArrayItem.getString("humidity"));

                weather.setIconToUse(currArrayItem.getString("icon"));
                weather.setConditions(currArrayItem.getString("condition"));

                String timeStamp = time.getString("weekday_name_abbrev") + " " + time.getString("civil");
                weather.setTimeOfWeather(timeStamp);

                parsedHourlyData.add(weather);
            }
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }

        return parsedHourlyData;
    }

    //Parses JSONObject for the daily data
    //Gets low / high temps, precipitation amount & chance, humidity, icon to display, conditions
    //that are related to the icon, wind speed & direction, and the date for that weather
    public ArrayList<WeatherInfo> parseDaily(JSONObject mainData)
    {
        ArrayList<WeatherInfo> parsedDailyData = new ArrayList<WeatherInfo>();

        //Tries to parse all the date in the object
        try
        {
            JSONObject forecast = mainData.getJSONObject("forecast");
            JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
            JSONArray dayData = simpleForecast.getJSONArray("forecastday");

            for(int i = 0; i < dayData.length(); i++)
            {
                JSONObject currItem = dayData.getJSONObject(i);
                JSONObject date = currItem.getJSONObject("date");
                JSONObject high = currItem.getJSONObject("high");
                JSONObject low = currItem.getJSONObject("low");
                JSONObject rain = currItem.getJSONObject("qpf_allday"); //might need one for snow and switch off depending on the weather?
                JSONObject wind = currItem.getJSONObject("avewind");

                WeatherInfo weather = new WeatherInfo(context);

                weather.setHighOfTemp(high.getString("celsius"));
                weather.setLowOfTemp(low.getString("celsius"));

                weather.setPrecipitationAmount(rain.getString("in"));
                weather.setPrecipitationChance(currItem.getString("pop"));

                weather.setHumidity(currItem.getString("avehumidity"));

                weather.setIconToUse(currItem.getString("icon"));
                weather.setConditions(currItem.getString("conditions"));

                weather.setWindSpeed(wind.getString("mph"));
                weather.setWindDirection(wind.getString("dir"));

                String timeStamp = date.getString("weekday_short") + " " + date.getString("monthname")
                        + " " + date.getString("day");
                weather.setTimeOfWeather(timeStamp);

                parsedDailyData.add(weather);
            }
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }

        return parsedDailyData;
    }
}
