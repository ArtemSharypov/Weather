# Weather
App that allows users to find the weather based on their current location, or based on a location that has been entered.   
It shows the current conditions out including the expected sunrise and sunset times, and lets users see the upcoming forecast for 
up to 10days in both hourly and day by day formats.  
Allows the customization of measurements as metric or imperial. 

# How it works
Uses Google Place API to have an autocomplete search function for locations, and for getting current or last known gps location.  
Then with the location or gps coordinates queries Wunderground API to find the current conditions and forecasts for that specific area
and fills the necessary information to display to the user.
