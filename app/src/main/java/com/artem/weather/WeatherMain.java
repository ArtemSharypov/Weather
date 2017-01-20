package com.artem.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WeatherMain
{
    private Context context;
    private String conditions;
    private Bitmap iconToUse;
    private String windSpeed;
    private String windDirection;
    private String humidity;

    public WeatherMain(Context context)
    {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Bitmap getIconToUse() {
        return iconToUse;
    }

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

    public void setIconToUse(Bitmap iconToUse) {
        this.iconToUse = iconToUse;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
