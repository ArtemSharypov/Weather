package com.artem.weather;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteArrayAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> locationList;

    public AutoCompleteArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        int size = 0;

        if(locationList.size() != 0)
            size = locationList.size();

        return size;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        final Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList<String> queryResults;

                if(charSequence != null || charSequence.length() == 0)
                {
                    queryResults = readData(charSequence.toString());
                }
                else
                {
                    queryResults = new ArrayList<>();
                }

                filterResults.values = queryResults;
                filterResults.count = queryResults.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                locationList = (ArrayList<String>) filterResults.values;

                if(filterResults != null && filterResults.count > 0)
                {
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return locationList.get(position);
    }

    //Adds in all locations from autoCompleteText into a ArrayList
    public ArrayList<String> readData(String autoCompleteText)
    {
        ArrayList<String> autoCompleteJSON = new ArrayList<>();

        try
        {
            JSONObject autoCompleteData = new JSONObject(autoCompleteText);
            JSONArray predictions = autoCompleteData.getJSONArray("predictions");
            JSONObject currItem;
            String location;

            for(int i = 0; i < predictions.length(); i++)
            {
                currItem = predictions.getJSONObject(i);
                location = currItem.getString("description");

                autoCompleteJSON.add(location);
            }
        }
        catch (JSONException error)
        {
            error.printStackTrace();
        }

        return autoCompleteJSON;
    }
}
