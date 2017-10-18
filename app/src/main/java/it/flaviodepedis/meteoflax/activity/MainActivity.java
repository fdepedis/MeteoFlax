package it.flaviodepedis.meteoflax.activity;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.flaviodepedis.meteoflax.R;
import it.flaviodepedis.meteoflax.common.GPSTracker;

public class MainActivity extends AppCompatActivity {

    //private final String ERROR_MESSAGE_EMPTY = getResources().getString(R.string.hint_insert_city_label);
    private final String ERROR_MESSAGE_EMPTY = "Insert city aame";
    private String city;

    @BindView(R.id.btnSubmit)
    Button btnSearchCity;
    @BindView(R.id.searchCity)
    EditText etCity;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    // GPSTracker class
    private GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create class object for get Location throught constructor
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            // Set city name into the EditText
            setEditText(gps);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        // click search button
        btnSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                city = etCity.getText().toString();

                if (city != null && !city.isEmpty()) {
                    Intent settingsIntent = new Intent(MainActivity.this, MeteoActivity.class);
                    settingsIntent.putExtra("cityMeteo", city);
                    startActivity(settingsIntent);
                } else {
                    Toast.makeText(getApplicationContext(), ERROR_MESSAGE_EMPTY, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                // Set city name to null to clear
                etCity.setText(null);

                // call constructor
                gps = new GPSTracker(MainActivity.this);

                // set EditText
                setEditText(gps);

                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void setEditText(GPSTracker gps) {
        city = gps.getCityName();
        etCity.setText(city);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}