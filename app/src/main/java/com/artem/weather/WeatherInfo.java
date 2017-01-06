package com.artem.weather;


public class WeatherInfo {
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
    private String iconToUse;
    private String dateUpdated;

    public WeatherInfo(String location, String iconToUse, String dateUpdated)
    {
        this.location = location;
        this.iconToUse = iconToUse;
        this.dateUpdated = dateUpdated;
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

    public String getIconToUse() {
        return iconToUse;
    }

    public void setIconToUse(String iconToUse) {
        this.iconToUse = iconToUse;
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
}
