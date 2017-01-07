package com.artem.weather;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WeatherInfo {
    private Context context;
    private String location;
    private String temperature;
    private String feelsLike;
    private String windSpeed;
    private String windDirection;
    private String windGust;
    private String precipitationChance;
    private String precipitationAmount;
    private String humidity;
    private String sunrise;
    private String sunset;
    private Bitmap iconToUse;
    private String dateUpdated;
    private String lowOfTemp;
    private String highOfTemp;

    public WeatherInfo(Context context)
    {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getPrecipitationChance() {
        return precipitationChance;
    }

    public void setPrecipitationChance(String precipitationChance) {
        this.precipitationChance = precipitationChance;
    }

    public String getPrecipitationAmount() {
        return precipitationAmount;
    }

    public void setPrecipitationAmount(String precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Bitmap getIconToUse() {
        return iconToUse;
    }

    //Sets the bitmap to the proper image depending on the conditions
    public void setIconToUse(String iconName) {
        switch(iconName)
        {
            //Day cases
            case "chanceflurries":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.chanceflurries);
                break;
            case "chancerain":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.chancerain);
                break;
            case "chancesleet":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.chancesleet);
                break;
            case "chancesnow":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.chancesnow);
                break;
            case "chancetstorms":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.chancetstorms);
                break;
            case "clear":
            case "sunny":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny);
                break;
            case "cloudy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.cloudy);
                break;
            case "flurries":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.flurries);
                break;
            case "fog":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.fog);
                break;
            case "hazy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.hazy);
                break;
            case "mostlycloudy":
            case "partlysunny":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.mostlycloudy);
                break;
            case "mostlysunny":
            case "partlycloudy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.mostlysunny);
                break;
            case "rain":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.rain);
                break;
            case "sleet":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.sleet);
                break;
            case "snow":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
                break;
            case "tstorms":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.tstorms);
                break;

            //Night cases
            case "nt_chanceflurries":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_chanceflurries);
                break;
            case "nt_chancerain":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_chancerain);
                break;
            case "nt_chancesleet":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_chancesleet);
                break;
            case "nt_chancesnow":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_chancesnow);
                break;
            case "nt_chancetstorms":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_chancetstorms);
                break;
            case "nt_clear":
            case "nt_sunny":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_sunny);
                break;
            case "nt_cloudy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_cloudy);
                break;
            case "nt_flurries":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_flurries);
                break;
            case "nt_fog":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_fog);
                break;
            case "nt_hazy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_hazy);
                break;
            case "nt_mostlycloudy":
            case "nt_partlysunny":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_mostlycloudy);
                break;
            case "nt_mostlysunny":
            case "nt_partlycloudy":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_mostlysunny);
                break;
            case "nt_rain":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_rain);
                break;
            case "nt_sleet":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_sleet);
                break;
            case "nt_snow":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_snow);
                break;
            case "nt_tstorms":
                this.iconToUse = BitmapFactory.decodeResource(context.getResources(), R.drawable.nt_tstorms);
                break;
        }

    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindGust() {
        return windGust;
    }

    public void setWindGust(String windGust) {
        this.windGust = windGust;
    }

    public String getLowOfTemp() {
        return lowOfTemp;
    }

    public void setLowOfTemp(String lowOfTemp) {
        this.lowOfTemp = lowOfTemp;
    }

    public String getHighOfTemp() {
        return highOfTemp;
    }

    public void setHighOfTemp(String highOfTemp) {
        this.highOfTemp = highOfTemp;
    }
}
