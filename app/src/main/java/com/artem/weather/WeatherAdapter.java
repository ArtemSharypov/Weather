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
        public TextView highOfTemp;
        public TextView lowOfTemp;
        public TextView windInfo;
        public TextView humidity;
        public TextView currConditions;
        public ImageView icon;

        public ViewHolderHourly(View itemView) {
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

    //Layout for forecast / 10day forecast
    public static class ViewHolderDaily extends RecyclerView.ViewHolder
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

        public ViewHolderDaily(View itemView) {
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
    private final int HOURLY = 0;
    private final int DAILY = 2;

    public WeatherAdapter(Context context, ArrayList<WeatherInfo> weatherList)
    {
        this.weatherList = weatherList;
        this.context = context;
    }

    //Inflates a layout and returns the holder for it
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType) {
            case HOURLY:
                Context pContext = parent.getContext();
                LayoutInflater inflater = LayoutInflater.from(pContext);

                //Creates a view with the specified layout
                View weatherView = inflater.inflate(R.layout.day_layout, parent, false);

                viewHolder = new ViewHolderHourly(weatherView);

                break;
            case DAILY:
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
            case HOURLY:
                ViewHolderHourly hourly = (ViewHolderHourly) holder;
                WeatherInfo weather = weatherList.get(position);

                //Sets all of the information in the view
                hourly.date.setText(weather.getTimeOfWeather());
                hourly.currentTemp.setText(weather.getTemperature());
                hourly.lowOfTemp.setText(weather.getLowOfTemp());
                hourly.highOfTemp.setText(weather.getHighOfTemp());
                hourly.windInfo.setText(weather.getWindSpeed() + weather.getWindDirection());
                hourly.humidity.setText(weather.getHumidity());
                hourly.icon.setImageBitmap(weather.getIconToUse());
                hourly.currConditions.setText(weather.getConditions());
                hourly.feelsLikeTemp.setText(weather.getFeelsLike());

                //Hides textviews depending on if there was any content to be shown or not
                if (weather.getLowOfTemp() != null)
                    hourly.lowOfTemp.setVisibility(View.INVISIBLE);
                else
                    hourly.lowOfTemp.setVisibility(View.VISIBLE);

                if (weather.getHighOfTemp() != null)
                    hourly.lowOfTemp.setVisibility(View.INVISIBLE);
                else
                    hourly.lowOfTemp.setVisibility(View.VISIBLE);

                if (weather.getTemperature() != null)
                    hourly.lowOfTemp.setVisibility(View.INVISIBLE);
                else
                    hourly.lowOfTemp.setVisibility(View.VISIBLE);

                if (weather.getFeelsLike() != null)
                    hourly.lowOfTemp.setVisibility(View.INVISIBLE);
                else
                    hourly.lowOfTemp.setVisibility(View.VISIBLE);

                break;

            case DAILY:
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
        //Can make it change numbers depending on the instanceof (class it is) for the weather
        //so check if instanceof hourly weather, if so return, if not return 2
        //bam bam
        return position % 2;
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