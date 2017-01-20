package com.artem.weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //Layout for hourly / 10day hourly weather
    public static class ViewHolderHourly extends RecyclerView.ViewHolder
    {
        public TextView date;
        public TextView currentTemp;
        public TextView feelsLikeTemp;
        public TextView windInfo;
        public TextView humidity;
        public TextView currConditions;
        public ImageView icon;

        public ViewHolderHourly(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            currentTemp = (TextView) itemView.findViewById(R.id.current_temp);
            feelsLikeTemp = (TextView) itemView.findViewById(R.id.feels_like);
            windInfo = (TextView) itemView.findViewById(R.id.wind_info);
            humidity = (TextView) itemView.findViewById(R.id.humidity);
            icon = (ImageView) itemView.findViewById(R.id.weather_icon);
            currConditions = (TextView) itemView.findViewById(R.id.day_conditions);
        }
    }

    //Layout for forecast / 10day forecast
    public static class ViewHolderDaily extends RecyclerView.ViewHolder
    {
        public TextView date;
        public TextView highOfTemp;
        public TextView lowOfTemp;
        public TextView windInfo;
        public TextView humidity;
        public TextView currConditions;
        public TextView precipAmt;
        public TextView precipChance;
        public ImageView icon;

        public ViewHolderDaily(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            highOfTemp = (TextView) itemView.findViewById(R.id.temp_high);
            lowOfTemp = (TextView) itemView.findViewById(R.id.current_temp);
            windInfo = (TextView) itemView.findViewById(R.id.wind_info);
            humidity = (TextView) itemView.findViewById(R.id.humidity);
            icon = (ImageView) itemView.findViewById(R.id.weather_icon);
            currConditions = (TextView) itemView.findViewById(R.id.day_conditions);
            precipAmt = (TextView) itemView.findViewById(R.id.precipitation_amount);
            precipChance = (TextView) itemView.findViewById(R.id.precipitation_chance);
        }
    }

    private ArrayList<WeatherMain> weatherList;
    private Context context;
    private final int HOURLY = 1;
    private final int DAILY = 2;

    public WeatherAdapter(Context context, ArrayList<WeatherMain> weatherList)
    {
        this.weatherList = weatherList;
        this.context = context;
    }

    //Inflates a layout and returns the holder for it
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder = null;
        Context pContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(pContext);
        View weatherView;

        //Switch between hourly or daily views depending on whats passed
        switch(viewType) {
            case HOURLY:
                weatherView = inflater.inflate(R.layout.hourly_layout, parent, false);
                viewHolder = new ViewHolderHourly(weatherView);

                break;
            case DAILY:
                weatherView = inflater.inflate(R.layout.day_layout, parent, false);
                viewHolder = new ViewHolderDaily(weatherView);

                break;
        }

        return viewHolder;
    }

    //Populates the data into the item via the holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch(holder.getItemViewType())
        {
            //Layout for hourly weather
            case HOURLY:
                ViewHolderHourly hourly = (ViewHolderHourly) holder;
                WeatherHourly weatherHourly = (WeatherHourly) weatherList.get(position);

                //Sets all of the information in the view
                hourly.date.setText(weatherHourly.getTimeOfWeather());
                hourly.currentTemp.setText(weatherHourly.getCurrentTemp());
                hourly.feelsLikeTemp.setText(weatherHourly.getFeelsLikeTemp());
                hourly.windInfo.setText(weatherHourly.getWindSpeed() + weatherHourly.getWindDirection());
                hourly.humidity.setText(weatherHourly.getHumidity());
                hourly.icon.setImageBitmap(weatherHourly.getIconToUse());
                hourly.currConditions.setText(weatherHourly.getConditions());

                break;

            //Layout for day by day weather
            case DAILY:
                ViewHolderDaily daily = (ViewHolderDaily) holder;
                WeatherDaily weatherDaily = (WeatherDaily) weatherList.get(position);

                //Set the information in the view
                daily.date.setText(weatherDaily.getTimeOfWeather());
                daily.highOfTemp.setText(weatherDaily.getHighOfTemp());
                daily.lowOfTemp.setText(weatherDaily.getLowOfTemp());
                daily.windInfo.setText(weatherDaily.getWindSpeed() + weatherDaily.getWindDirection());
                daily.humidity.setText(weatherDaily.getHumidity());
                daily.currConditions.setText(weatherDaily.getConditions());
                daily.precipChance.setText(weatherDaily.getPrecipitationChance());
                daily.precipAmt.setText(weatherDaily.getPrecipitationAmt());
                daily.icon.setImageBitmap(weatherDaily.getIconToUse());

                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return weatherList.size();
    }

    @Override
    public int getItemViewType(int position) {
        WeatherMain firstWeather = weatherList.get(0);
        int viewType = 0;

        //Check if its hourly or daily weather information
        if(firstWeather instanceof WeatherHourly)
            viewType = HOURLY;
        else if(firstWeather instanceof WeatherDaily)
            viewType = DAILY;

        return viewType;
    }

    //Swaps the weather info being shown in the RecyclerView currently
    public void swap(ArrayList<WeatherMain> newWeatherList)
    {
        //Just in case its empty
        if(weatherList != null)
        {
            weatherList.clear();
            weatherList.addAll(newWeatherList);
        }
        else
        {
            weatherList = newWeatherList;
        }

        notifyDataSetChanged();
    }
}