package com.artem.weather;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class JSONParser {

    private Context context;
    private SharedPreferences prefs;
    private final boolean timeFormat12Hr;
    private final boolean precipitationMM;
    private final boolean tempInCelsius;
    private final boolean windInMPH;

    public JSONParser(Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences("weather", MODE_PRIVATE);

        timeFormat12Hr= prefs.getBoolean("12HourFormat", true);
        precipitationMM = prefs.getBoolean("precipitationMM", true);
        tempInCelsius = prefs.getBoolean("temperatureCelsius", true);
        windInMPH = prefs.getBoolean("windMPH", true);
    }

    //Parses location, date the weather was last updated, temperature, what it feels like out,
    //humidity, precipitation amount, wind speed / direction / gust, and the icon to display and
    //the current conditions
    public WeatherInfo parseConditions(JSONObject conditionsJSON)
    {
        WeatherInfo weather = new WeatherInfo(context);

        String temperature;
        String feelsLike;
        String wind;
        String windGust;
        String precipitation;

        try
        {
            JSONObject currentObservation = conditionsJSON.getJSONObject("current_observation");
            JSONObject displayLocation = currentObservation.getJSONObject("display_location");

            //switch between Celsius and Fahrenheit
            if(tempInCelsius)
            {
                temperature = currentObservation.getString("temp_c") + "°C";
                feelsLike = currentObservation.getString("feelslike_c")+ "°C";
            }
            else
            {
                temperature = currentObservation.getString("temp_f") + "°F";
                feelsLike = currentObservation.getString("feelslike_f")+ "°F";
            }

            //Switch between mm and in
            if(precipitationMM)
                precipitation = currentObservation.getString("precip_today_metric") + "mm";
            else
                precipitation = currentObservation.getString("precip_today_in") + "in";

            //Changes windspeeds to MPH or KPH
            if(windInMPH)
            {
                wind = currentObservation.getString("wind_mph") + "mph ";
                windGust = currentObservation.getString("wind_gust_mph") + "mph";
            }
            else
            {
                wind = currentObservation.getString("wind_kph") + "kph ";
                windGust = currentObservation.getString("wind_gust_kph") + "kph";
            }

            weather.setLocation(displayLocation.getString("full"));
            weather.setDateUpdated(currentObservation.getString("observation_time"));

            weather.setTemperature("Curr Temp: " + temperature);
            weather.setFeelsLike("Feels Like: " + feelsLike);

            weather.setHumidity("Humidity: " + currentObservation.getString("relative_humidity") + "%");
            weather.setPrecipitationAmount("Snow/Rain " + precipitation);

            weather.setWindSpeed("Wind: " + wind);
            weather.setWindDirection(currentObservation.getString("wind_dir"));
            weather.setWindGust("Gusts of " + windGust);

            weather.setIconToUse(currentObservation.getString("icon"));
            weather.setConditions(currentObservation.getString("weather"));
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
    public ArrayList<WeatherInfo> parseHourly(JSONObject hourlyJSON)
    {
        ArrayList<WeatherInfo> parsedHourlyData = new ArrayList<>();

        String currTemp;
        String feelsLikeTemp;
        String wind;
        String timeStamp;

        try
        {
            JSONArray hourlyForecast = hourlyJSON.getJSONArray("hourly_forecast");

            //Parses all of the data and adds it to the list
            for(int i = 0; i < hourlyForecast.length(); i++)
            {
                JSONObject currArrayItem = hourlyForecast.getJSONObject(i);
                JSONObject time = currArrayItem.getJSONObject("FCTTIME");
                JSONObject temperature = currArrayItem.getJSONObject("temp");
                JSONObject windSpeed = currArrayItem.getJSONObject("wspd");
                JSONObject windDir = currArrayItem.getJSONObject("wdir");
                JSONObject feelsLike = currArrayItem.getJSONObject("feelslike");

                timeStamp = time.getString("weekday_name_abbrev") + " ";

                //switch between Celsius and Fahrenheit
                if(tempInCelsius)
                {
                    currTemp =  temperature.getString("metric") + "°C";
                    feelsLikeTemp = feelsLike.getString("metric")+ "°C";
                }
                else
                {
                    currTemp = temperature.getString("english") + "°F";
                    feelsLikeTemp = feelsLike.getString("english")+ "°F";
                }

                //changes windspeeds to MPH or KPH
                if(windInMPH)
                    wind = windSpeed.getString("metric") + "mph ";
                else
                    wind = windSpeed.getString("english") + "kph ";

                //change between 12 hour and 24 hour time format
                if(timeFormat12Hr)
                    timeStamp += time.getString("civil");
                else
                    timeStamp += time.getString("hour") + ":" + time.getString("min");

                WeatherInfo weather = new WeatherInfo(context);

                weather.setTemperature("Curr Temp: " + currTemp);
                weather.setFeelsLike("Feels Like: " + feelsLikeTemp);

                weather.setWindSpeed("Wind: " + wind);
                weather.setWindDirection(windDir.getString("dir"));

                weather.setHumidity("Humidity: " + currArrayItem.getString("humidity") + "%");

                weather.setIconToUse(currArrayItem.getString("icon"));
                weather.setConditions(currArrayItem.getString("condition"));

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
    public ArrayList<WeatherInfo> parseDaily(JSONObject dailyJSON)
    {
        ArrayList<WeatherInfo> parsedDailyData = new ArrayList<WeatherInfo>();

        String tempLow;
        String tempHigh;
        String windSpeed;
        String precipitation;

        //Tries to parse all the date in the object
        try
        {
            JSONObject forecast = dailyJSON.getJSONObject("forecast");
            JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastday = simpleForecast.getJSONArray("forecastday");

            for(int i = 0; i < forecastday.length(); i++)
            {
                JSONObject currItem = forecastday.getJSONObject(i);
                JSONObject date = currItem.getJSONObject("date");
                JSONObject high = currItem.getJSONObject("high");
                JSONObject low = currItem.getJSONObject("low");
                JSONObject rain = currItem.getJSONObject("qpf_allday");
                JSONObject wind = currItem.getJSONObject("avewind");

                //switch between Celsius and Fahrenheit
                if(tempInCelsius)
                {
                    tempLow = high.getString("celsius") + "°C";
                    tempHigh = low.getString("celsius")+ "°C";
                }
                else
                {
                    tempLow = high.getString("fahrenheit") + "°F";
                    tempHigh = low.getString("fahrenheit")+ "°F";
                }

                //Switch between mm and in
                if(precipitationMM)
                    precipitation = rain.getString("mm") + "mm";
                else
                    precipitation = rain.getString("in") + "in";

                //Changes windspeeds to MPH or KPH
                if(windInMPH)
                    windSpeed = wind.getString("mph") + "mph ";
                else
                    windSpeed = wind.getString("kph") + "kph ";

                WeatherInfo weather = new WeatherInfo(context);

                weather.setHighOfTemp("High Of: " + tempHigh);
                weather.setLowOfTemp("Low Of: " + tempLow);

                weather.setPrecipitationAmount("Snow/Rain" + precipitation);
                weather.setPrecipitationChance("Precipitation %" + currItem.getString("pop") + "%");

                weather.setHumidity("Humidity: " + currItem.getString("avehumidity") + "%");

                weather.setIconToUse(currItem.getString("icon"));
                weather.setConditions(currItem.getString("conditions"));

                weather.setWindDirection(wind.getString("dir"));
                weather.setWindSpeed("Wind: " + windSpeed);

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

    public ArrayList<String> parseAstronomy(JSONObject astronomyJSON)
    {
        ArrayList<String> sunData = new ArrayList<>();

        try
        {
            JSONObject moonPhase = astronomyJSON.getJSONObject("moon_phase");
            JSONObject sunrise = moonPhase.getJSONObject("sunrise");
            JSONObject sunset = moonPhase.getJSONObject("sunset");

            String sunriseTime = "";
            String sunsetTime = "";
            String temp;
            Long tempNumb;

            if(timeFormat12Hr)
            {
                temp = sunrise.getString("hour");
                tempNumb = Long.parseLong(temp);

                //Switch to PM format if its past noon
                if(tempNumb > 12)
                {
                    tempNumb -= 12;
                    sunriseTime = tempNumb + ":" + sunrise.getString("minute") + "pm";
                }
                else
                {
                    sunriseTime = temp + ":" + sunrise.getString("minute") + "am";
                }

                temp = sunset.getString("hour");
                tempNumb = Long.parseLong(temp);

                //Switch to PM format if its past noon
                if(tempNumb > 12)
                {
                    tempNumb -= 12;
                    sunsetTime = tempNumb + ":" + sunset.getString("minute") + "pm";
                }
                else
                {
                    sunsetTime = temp + ":" + sunset.getString("minute") + "am";
                }
            }
            else
            {
                sunriseTime = sunrise.getString("hour") + ":" + sunrise.getString("minute");
                sunsetTime = sunset.getString("hour") + ":" + sunset.getString("minute");
            }

            sunData.add(sunriseTime);
            sunData.add(sunsetTime);
        }
        catch(JSONException error)
        {
            error.printStackTrace();
        }

        return sunData;
    }
}
