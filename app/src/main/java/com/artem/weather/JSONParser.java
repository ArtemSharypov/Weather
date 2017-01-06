package com.artem.weather;

//parses the stuff we get back from query
//method for each type of thing we parse for
public class JSONParser {
    public JSONParser()
    {

    }


    //all will create a WeatherInfo object that gets returned with either a string city name or location
    //parse conditions

    //for forecast & 10days just need a private method that'll parse a single period / day object and return it
    //should be a ton easier
    //parse forecast

    //parse forecast 10days

    //for hourly could just make another private method that parses the hour for the information needed and return it
    //should work for any hourly conditions
    //parse hourly

    //parse hourly 10days (only using 48 hours though)


    //for conditions
    //look at current_observation
    //looks at display_location JSON object
        //from which can call nameofit.getString(nameoftheitemyouwanttogethere)
    //rest can be called from current_observation.getString(nameofstuff)


    /*
    {
  "display_location": {
  "full": "San Francisco, CA",
  "city": "San Francisco",
  "state": "CA",
  "state_name": "California",
  "country": "US",
  "country_iso3166": "US",
  "zip": "94101",
  "latitude": "37.77500916",
  "longitude": "-122.41825867",
  "elevation": "47.00000000"
  },
  "observation_location": {
  "full": "SOMA - Near Van Ness, San Francisco, California",
  "city": "SOMA - Near Van Ness, San Francisco",
  "state": "California",
  "country": "US",
  "country_iso3166": "US",
  "latitude": "37.773285",
  "longitude": "-122.417725",
  "elevation": "49 ft"
  },
  "estimated": {},
  "station_id": "KCASANFR58",
  "observation_time": "Last Updated on June 27, 5:27 PM PDT",
  "observation_time_rfc822": "Wed, 27 Jun 2012 17:27:13 -0700",
  "observation_epoch": "1340843233",
  "local_time_rfc822": "Wed, 27 Jun 2012 17:27:14 -0700",
  "local_epoch": "1340843234",
  "local_tz_short": "PDT",
  "local_tz_long": "America/Los_Angeles",
  "local_tz_offset": "-0700",
  "weather": "Partly Cloudy",
  "temperature_string": "66.3 F (19.1 C)",
  "temp_f": 66.3,
  "temp_c": 19.1,
  "relative_humidity": "65%",
  "wind_string": "From the NNW at 22.0 MPH Gusting to 28.0 MPH",
  "wind_dir": "NNW",
  "wind_degrees": 346,
  "wind_mph": 22.0,
  "wind_gust_mph": "28.0",
  "wind_kph": 35.4,
  "wind_gust_kph": "45.1",
  "pressure_mb": "1013",
  "pressure_in": "29.93",
  "pressure_trend": "+",
  "dewpoint_string": "54 F (12 C)",
  "dewpoint_f": 54,
  "dewpoint_c": 12,
  "heat_index_string": "NA",
  "heat_index_f": "NA",
  "heat_index_c": "NA",
  "windchill_string": "NA",
  "windchill_f": "NA",
  "windchill_c": "NA",
  "feelslike_string": "66.3 F (19.1 C)",
  "feelslike_f": "66.3",
  "feelslike_c": "19.1",
  "visibility_mi": "10.0",
  "visibility_km": "16.1",
  "solarradiation": "",
  "UV": "5",
  "precip_1hr_string": "0.00 in ( 0 mm)",
  "precip_1hr_in": "0.00",
  "precip_1hr_metric": " 0",
  "precip_today_string": "0.00 in (0 mm)",
  "precip_today_in": "0.00",
  "precip_today_metric": "0",
  "icon": "partlycloudy",
  }
}
     */
}
