package com.artem.weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>
{
    //Layout for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView date;
        public TextView currentTemp;
        public TextView feelsLikeTemp;
        public TextView highOfTemp;
        public TextView lowOfTemp;
        public TextView windInfo;
        public TextView humidity;
        public TextView currConditions;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            currentTemp = (TextView) itemView.findViewById(R.id.temperature);
            feelsLikeTemp = (TextView) itemView.findViewById(R.id.feels_like);
            highOfTemp = (TextView) itemView.findViewById(R.id.temp_high);
            lowOfTemp = (TextView) itemView.findViewById(R.id.temp_low);
            windInfo = (TextView) itemView.findViewById(R.id.wind_info);
            humidity = (TextView) itemView.findViewById(R.id.humidity);
            icon = (ImageView) itemView.findViewById(R.id.weather_icon);
            currConditions = (TextView) itemView.findViewById(R.id.day_conditions);
        }
    }

    private ArrayList<WeatherInfo> weatherList;
    private Context context;

    public WeatherAdapter(Context context, ArrayList<WeatherInfo> weatherList)
    {
        this.weatherList = weatherList;
        this.context = context;
    }

    //Inflates a layout and returns the holder for it
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context pContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(pContext);

        //Creates a view with the specified layout
        View weatherView = inflater.inflate(R.layout.day_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(weatherView);

        return viewHolder;
    }

    //Populates the data into the item via the holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        WeatherInfo weather = weatherList.get(position);

        //need to move high of and such into the weatherInfo itself
        //Sets all of the information in the view
        holder.date.setText(weather.getTimeOfWeather());
        holder.currentTemp.setText("Current Temp: " + weather.getTemperature() + "°C");
        holder.lowOfTemp.setText("Low Of: " + weather.getLowOfTemp() + "°C");
        holder.highOfTemp.setText("High Of: " +weather.getHighOfTemp() + "°C");
        holder.windInfo.setText("Wind: " + weather.getWindSpeed() + " " +
                weather.getWindDirection() + "mph ");
        holder.humidity.setText("Humidity: " + weather.getHumidity());
        holder.icon.setImageBitmap(weather.getIconToUse());
        holder.currConditions.setText(weather.getConditions());
    }

    @Override
    public int getItemCount()
    {
        return weatherList.size();
    }

    //Swaps the weather info being shown in the RecyclerView currently
    public void swap(ArrayList<WeatherInfo> newWeatherList)
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