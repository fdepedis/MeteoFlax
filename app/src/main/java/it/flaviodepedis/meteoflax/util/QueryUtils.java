package it.flaviodepedis.meteoflax.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import it.flaviodepedis.meteoflax.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.flaviodepedis.meteoflax.model.Meteo;

/**
 * Created by flavio.depedis on 12/05/2017.
 */

/**
 * Helper methods related to requesting and receiving meteo data from OpenWeatherMap.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Context of the caller activity
     */
    private static Context mContext;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the OpenWeatherMap dataset and return a list of {@link Meteo} objects.
     */
    public static List<Meteo> fetchMeteoData(String requestUrl, Context context) {

        Log.i(LOG_TAG, "Log - in fetchMeteoData() method");

        mContext = context;

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Meteo}s
        List<Meteo> meteos = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return meteos;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.w(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Meteo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Meteo> extractFeatureFromJson(String meteoJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(meteoJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding meteos to
        List<Meteo> meteos = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject baseJsonResponse;                    //JSONObject for response string data retrieved
            JSONObject meteoCity;                           //JSONObject for data retrieved from "city" object
            JSONObject coordCity;                           //JSONObject for data retrieved from "coord" object
            JSONObject currentMeteoList;                    //JSONObject for current meteos items
            JSONObject propMain;                            //JSONObject for data retrieved from "main" object

            JSONArray meteoArray;                           //JSONArray associated with the key "list" which represents a list of features (or meteo).
            JSONArray weatherArray;                         //JSONArray for data retrieved from "weather" object array

            String city = "";
            String country = "";
            String latitude = "";
            String longitude = "";
            String dateTime = "";
            String temp = "";
            String minTemp = "";
            String maxTemp = "";
            String pressure = "";
            String seaLevel = "";
            String humidity = "";
            int id;
            String main = null;
            String description = null;
            String icon = null;
            String wind = "";
            String deg = "";


            baseJsonResponse = new JSONObject(meteoJSON);

            // verify if "city" exists
            if (baseJsonResponse.has("city")) {
                meteoCity = baseJsonResponse.getJSONObject("city");

                // verify if "name" exists
                if (meteoCity.has("name")) {
                    city = meteoCity.getString("name");
                } else {
                    city = mContext.getResources().getString(R.string.empty_city_label);
                }

                // verify if "country" exists
                if (meteoCity.has("country")) {
                    country = meteoCity.getString("country");
                } else {
                    city = mContext.getResources().getString(R.string.empty_country_label);
                }

                // verify if "coord" JSONObject exists
                if (meteoCity.has("coord")) {
                    coordCity = meteoCity.getJSONObject("coord");

                    // verify if "lat" exists
                    if (coordCity.has("lat")) {
                        latitude = coordCity.getString("lat");
                    } else {
                        latitude = mContext.getResources().getString(R.string.empty_latitude_label);
                    }

                    // verify if "lon" exists
                    if (coordCity.has("lon")) {
                        longitude = coordCity.getString("lon");
                    } else {
                        longitude = mContext.getResources().getString(R.string.empty_longitude_label);
                    }
                }
            }

            // verify if "list" exists
            if (baseJsonResponse.has("list")) {
                meteoArray = baseJsonResponse.getJSONArray("list");

                // For each meteo in the meteoArray, create an {@link Meteo} object
                for (int i = 0; i < meteoArray.length(); i++) {

                    // Get a single meteo at position i within the list of meteos
                    currentMeteoList = meteoArray.getJSONObject(i);

                    // verify if "dt_txt" exists
                    if (currentMeteoList.has("dt_txt")) {
                        dateTime = currentMeteoList.getString("dt_txt");
                    } else {
                        dateTime = mContext.getResources().getString(R.string.empty_date_label);
                    }

                    // For a given meteo, extract the JSONObject associated with the
                    // key called "main", which represents a list of all properties
                    // for that meteo.
                    if (currentMeteoList.has("main")) {
                        propMain = currentMeteoList.getJSONObject("main");

                        // verify if "temp" exists
                        if (propMain.has("temp")) {
                            temp = propMain.getString("temp");
                        } else {
                            temp = mContext.getResources().getString(R.string.empty_temp_label);
                        }

                        // verify if "temp_min" exists
                        if (propMain.has("temp_min")) {
                            minTemp = propMain.getString("temp_min");
                        } else {
                            minTemp = mContext.getResources().getString(R.string.empty_mim_temp_label);
                        }

                        // verify if "temp_max" exists
                        if (propMain.has("temp_max")) {
                            maxTemp = propMain.getString("temp_max");
                        } else {
                            maxTemp = mContext.getResources().getString(R.string.empty_max_temp_label);
                        }

                        // verify if "pressure" exists
                        if (propMain.has("pressure")) {
                            pressure = propMain.getString("pressure");
                        } else {
                            pressure = mContext.getResources().getString(R.string.empty_pressure_label);
                        }

                        // verify if "sea_level" exists
                        if (propMain.has("sea_level")) {
                            seaLevel = propMain.getString("sea_level");
                        } else {
                            seaLevel = mContext.getResources().getString(R.string.empty_sea_level_label);
                        }

                        // verify if "humidity" exists
                        if (propMain.has("humidity")) {
                            humidity = propMain.getString("humidity");
                        } else {
                            humidity = mContext.getResources().getString(R.string.empty_humidity_label);
                        }
                    }

                    // Extract the JSONArray associated with the key called "weather"
                    // inside the "list" JSONArray
                    weatherArray = new JSONArray(meteoArray.getJSONObject(i).getString("weather"));

                    for (int j = 0; j < weatherArray.length(); j++) {
                        id = weatherArray.getJSONObject(j).getInt("id");
                        main = weatherArray.getJSONObject(j).getString("main");
                        description = weatherArray.getJSONObject(j).getString("description");
                        icon = weatherArray.getJSONObject(j).getString("icon");
                    }

                    // Extract the JSONObject associated to "wind" key
                    if (currentMeteoList.has("wind")) {
                        JSONObject propWind = currentMeteoList.getJSONObject("wind");

                        // verify if "speed" exists
                        if (propWind.has("speed")) {
                            wind = propWind.getString("speed");
                        } else {
                            wind = mContext.getResources().getString(R.string.empty_speed_label);
                        }

                        // verify if "deg" exists
                        if (propWind.has("deg")) {
                            deg = propWind.getString("deg");
                        } else {
                            deg = mContext.getResources().getString(R.string.empty_degree_label);
                        }
                    }
                    // Create a new {@link Meteo} object with the dateTime, minTemp, maxTemp, desc,
                    // icon, wind from the JSON response.
                    Meteo meteo = new Meteo(dateTime, temp, minTemp, maxTemp, description, icon, wind,
                            city, country, latitude, longitude, pressure, seaLevel, deg, humidity);

                    // Add the new {@link Meteo} to the list of meteos.
                    meteos.add(meteo);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the meteo JSON results", e);
        }

        // Return the list of meteos
        return meteos;
    }

    public static Drawable getBackgroundIcon(Context context, String icon) {
        int iconResourceID;
        switch (icon) {
            case "01d":
                iconResourceID = R.drawable.a01d;
                break;
            case "01n":
                iconResourceID = R.drawable.a01n;
                break;
            case "02d":
                iconResourceID = R.drawable.a02d;
                break;
            case "02n":
                iconResourceID = R.drawable.a02n;
                break;
            case "03d":
                iconResourceID = R.drawable.a03d;
                break;
            case "03n":
                iconResourceID = R.drawable.a03n;
                break;
            case "04d":
                iconResourceID = R.drawable.a04d;
                break;
            case "04n":
                iconResourceID = R.drawable.a04n;
                break;
            case "09d":
                iconResourceID = R.drawable.a09d;
                break;
            case "09n":
                iconResourceID = R.drawable.a09n;
                break;
            case "10d":
                iconResourceID = R.drawable.a10d;
                break;
            case "10n":
                iconResourceID = R.drawable.a10n;
                break;
            case "11d":
                iconResourceID = R.drawable.a11d;
                break;
            case "11n":
                iconResourceID = R.drawable.a11n;
                break;
            case "13d":
                iconResourceID = R.drawable.a13d;
                break;
            case "13n":
                iconResourceID = R.drawable.a13n;
                break;
            case "50d":
                iconResourceID = R.drawable.a50d;
                break;
            case "50n":
                iconResourceID = R.drawable.a50n;
                break;
            default:
                iconResourceID = R.drawable.ic_shortcut_close;
                break;
        }

        return ContextCompat.getDrawable(context, iconResourceID);
    }

}