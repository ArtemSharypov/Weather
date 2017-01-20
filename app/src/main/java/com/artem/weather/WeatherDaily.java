package com.artem.weather;

import android.content.Context;

public class WeatherDaily extends WeatherMain
{
    private String highOfTemp;
    private String lowOfTemp;
    private String precipitationAmt;
    private String precipitationChance;
    private String timeOfWeather;

    public WeatherDaily(Context context)
    {
        super(context);
    }

    public String getHighOfTemp() {
        return highOfTemp;
    }

    public void setHighOfTemp(String highOfTemp) {
        this.highOfTemp = highOfTemp;
    }

    public String getLowOfTemp() {
        return lowOfTemp;
    }

    public void setLowOfTemp(String lowOfTemp) {
        this.lowOfTemp = lowOfTemp;
    }

    public String getPrecipitationAmt() {
        return precipitationAmt;
    }

    public void setPrecipitationAmt(String precipitationAmt) {
        this.precipitationAmt = precipitationAmt;
    }

    public String getPrecipitationChance() {
        return precipitationChance;
    }

    public void setPrecipitationChance(String precipitationChance) {
        this.precipitationChance = precipitationChance;
    }

    public String getTimeOfWeather() {
        return timeOfWeather;
    }

    public void setTimeOfWeather(String timeOfWeather) {
        this.timeOfWeather = timeOfWeather;
    }
}
