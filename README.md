# MeteoFlax
An Application developed by Flavio De Pedis

MeteoFlax App
===================================

This App displays a meteo using the webservice on Open Weather Map organization.

The implemented logic send a request to API webservice and receive JSON response that is parsed
in a ListView with a custom Adapter.

The request is base on 5 day forecast available at any location or city. 
It includes weather data every 3 hours.

The App contain also the geolocalization to retrieve automatic position and get information about
weather.

For info check: https://openweathermap.org/ 

This App is build with the knowledge achieved in the Udacity course in the Beginning Android Nanodegree.

Main features used:

- Loader
- AsyncTaskLoader
- ArrayAdapter
- List in main activity inflate through ArrayAdatper
- JSON
- Menu
- SharedPreference
- LocationListener for geolocalization

Pre-requisites
--------------

- Android SDK v26
- Android Build Tools v26.0.0
- Android Support Repository v26.0.0

