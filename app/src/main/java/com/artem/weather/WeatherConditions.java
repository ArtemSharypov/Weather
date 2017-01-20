package com.artem.weather;

import android.content.Context;

public class WeatherConditions extends WeatherMain
{
    private String lastUpdated;
    private String location;
    private String currentTemp;
    private String feelsLikeTemp;
    private String precipitationAmt;
    private String windGust;

    public WeatherConditions(Context context)
    {
        super(context);
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPrecipitationAmt() {
        return precipitationAmt;
    }

    public void setPrecipitationAmt(String precipitationAmt) {
        this.precipitationAmt = precipitationAmt;
    }

    public String getWindGust() {
        return windGust;
    }

    public void setWindGust(String windGust) {
        this.windGust = windGust;
    }
}
