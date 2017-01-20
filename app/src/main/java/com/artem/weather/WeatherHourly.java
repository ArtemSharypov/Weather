package com.artem.weather;

import android.content.Context;

public class WeatherHourly extends WeatherMain
{
    private String currentTemp;
    private String feelsLikeTemp;
    private String timeOfWeather;

    public WeatherHourly(Context context) {
        super(context);
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(String feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public String getTimeOfWeather() {
        return timeOfWeather;
    }

    public void setTimeOfWeather(String timeOfWeather) {
        this.timeOfWeather = timeOfWeather;
    }
}
