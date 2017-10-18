package it.flaviodepedis.meteoflax.activity;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import java.util.ArrayList;
import java.util.List;

import it.flaviodepedis.meteoflax.R;
import it.flaviodepedis.meteoflax.model.Meteo;
import it.flaviodepedis.meteoflax.loader.MeteoLoader;
import it.flaviodepedis.meteoflax.adapter.MeteoAdapter;

public class MeteoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Meteo>> {

    private static final String LOG_TAG = MeteoActivity.class.getName();

    /**
     * Constant value for the meteo APPID.
     */
    private final String APPID = "3cd533246ca81712d620ac60a3cc3423";

    /**
     * Constant value for city. The value is inserted into MainActivity.
     */
    private static String city = null;

    /**
     * Constant value for the meteo loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int METEO_LOADER_ID = 1;

    /**
     * URL for meteo data from the openweathermap.org dataset
     */
    private static String OPEN_WEATHER_MAP_REQUEST_URL =
            "http://api.openweathermap.org/data/2.5/forecast";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Adapter for the list of meteo
     */
    private MeteoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);

        // Find a reference to the {@link ListView} in the layout
        ListView meteoListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        meteoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of meteo as input
        mAdapter = new MeteoAdapter(this, new ArrayList<Meteo>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        meteoListView.setAdapter(mAdapter);

        meteoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current meteo that was clicked on
                Meteo currentMeteo = mAdapter.getItem(position);
                city = currentMeteo.getCityMeteo();

                // Create a new intent to view the meteo URI
                Intent intent = new Intent(MeteoActivity.this, MeteoDetailActivity.class);
                intent.putExtra("currentMeteo", currentMeteo);

                // Send the intent to launch a new activity
                startActivity(intent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "Log - in before initLoader() call");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(METEO_LOADER_ID, null, this);

            Log.i(LOG_TAG, "Log - in after initLoader() call");
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.empty_internet_connection_label);
        }
    }

    @Override
    public Loader<List<Meteo>> onCreateLoader(int i, Bundle bundle) {

        Intent intent = getIntent();
        city = intent.getStringExtra("cityMeteo");

        // Set the title of ActionBar
        setTitle(getResources().getText(R.string.meteo_label) + ": " + city);

        // Costruisce la URL da inviare, prendendo i valori impostati nelle Preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countEvent = sharedPrefs.getString(
                getString(R.string.settings_count_key),
                getString(R.string.settings_count_default));

        String languages = sharedPrefs.getString(
                getString(R.string.settings_languages_key),
                getString(R.string.settings_languages_default));

        Uri baseUri = Uri.parse(OPEN_WEATHER_MAP_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("lang", languages);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("cnt", countEvent);
        uriBuilder.appendQueryParameter("APPID", APPID);
        uriBuilder.appendQueryParameter("q", city);

        // Decomment toast message for debug
        //Toast.makeText(this, uriBuilder.toString(), Toast.LENGTH_SHORT).show();
        //http://api.openweathermap.org/data/2.5/forecast?lang=it&units=metric&cnt=10&APPID=3cd533246ca81712d620ac60a3cc3423&q=Roma

        Log.w(LOG_TAG, "Log - in onCreateLoader() method" + " - " +  uriBuilder.toString());

        // Create a new loader for the given URL
        return new MeteoLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Meteo>> loader, List<Meteo> meteo) {

        Log.i(LOG_TAG, "Log - in onLoadFinished() method");

        // Set the title of ActionBar
        setTitle(getResources().getText(R.string.meteo_label) + ": " + city );

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No meteo found."
        mEmptyStateTextView.setText(R.string.empty_meteo_label);

        // Clear the adapter of previous meteo data
        mAdapter.clear();

        // If there is a valid list of {@link Meteo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (meteo != null && !meteo.isEmpty()) {
            mAdapter.addAll(meteo);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Meteo>> loader) {

        Log.i(LOG_TAG, "Log - in onLoaderReset() method");

        // Set the title of ActionBar
        setTitle(getResources().getText(R.string.meteo_label) + ": " + city);

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


    // When you click the back button on the ActionBar, you must be passing an intent
    // to create a new Activity. What you need to do is check if the Activity already exists or not.
    // If it does, then you need to resume it. Otherwise, create a new one.
    /*
    Quando fai clic sul pulsante Indietro sulla ActionBar, devi passare un'intent per creare una
    nuova Activity. Quello che devi fare è controllare se l'attività esiste già o no.
    Se esiste, allora devi riprendere la stessa Activity. Altrimenti, creane una nuova.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
