package com.artem.weather;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>
{
    //Layout for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        //holds all textviews and such that it'll have
        //using public to make the outer class have easier access

        public ViewHolder(View itemView) {
            super(itemView);

            //set the buttons and such
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
        View weatherView = inflater.inflate(R.layout.recycler_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(weatherView);

        return viewHolder;
    }

    //Populates the data into the item via the holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        WeatherInfo weather = weatherList.get(position);

        //this part sets the info that needs to be set
        //gotta set all TextViews and such from ViewHolder, just need to do holder.namehere
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