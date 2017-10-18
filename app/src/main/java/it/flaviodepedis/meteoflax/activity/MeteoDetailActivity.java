package it.flaviodepedis.meteoflax.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.flaviodepedis.meteoflax.R;
import it.flaviodepedis.meteoflax.model.Meteo;
import it.flaviodepedis.meteoflax.util.QueryUtils;

public class MeteoDetailActivity extends AppCompatActivity {

    private String city;
    private String latitude;
    private String longitude;

    @BindView(R.id.tvCityMeteo) TextView tvCityMeteo;
    @BindView(R.id.tvIconMeteo) TextView tvIconMeteo;
    @BindView(R.id.tvDescMeteo) TextView tvDescMeteo;
    @BindView(R.id.tvCountryMeteo) TextView tvCountryMeteo;
    @BindView(R.id.tvMinTempMeteoValue) TextView tvMinTempMeteo;
    @BindView(R.id.tvMaxTempMeteoValue) TextView tvMaxTempMeteo;
    @BindView(R.id.tvLatitudeMeteoValue) TextView tvLatMeteo;
    @BindView(R.id.tvLongitudeMeteoValue) TextView tvLonMeteo;
    @BindView(R.id.tvPressureMeteoValue) TextView tvPressureMeteo;
    @BindView(R.id.tvHumidityMeteoValue) TextView tvHumidityMeteo;
    @BindView(R.id.tvSeaLevelMeteoValue) TextView tvSeaLevelMeteo;
    @BindView(R.id.tvWindSpeedMeteoValue) TextView tvWindMeteo;
    @BindView(R.id.tvDegWindMeteoValue) TextView tvDegreeWindMeteo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo_detail);
        ButterKnife.bind(this);

        // get serializable object
        final Meteo currentMeteo = (Meteo) getIntent().getSerializableExtra("currentMeteo");

        /** Get Intent Extras */
        if (getIntent().getExtras() != null) {

            city = currentMeteo.getCityMeteo();

            setTitle(getResources().getText(R.string.meteo_detail_label) + ": " + city);

            // set city of current meteo search
            tvCityMeteo.setText(city);

            // set icon of current meteo
            Drawable takeIconMeteo = QueryUtils.getBackgroundIcon(getApplicationContext(),
                    currentMeteo.getIconMeteo());
            tvIconMeteo.setBackground(takeIconMeteo);

            // set the desc of current meteo
            tvDescMeteo.setText(currentMeteo.getDescMeteo());

            // set country of current meteo
            tvCountryMeteo.setText(currentMeteo.getCountryMeteo());

            // set the min temp of current meteo
            tvMinTempMeteo.setText(currentMeteo.getMinTempMeteo() + " °C");

            // set the max temp of current meteo
            tvMaxTempMeteo.setText(currentMeteo.getMaxTempMeteo() + " °C");

            // set the latitude of current meteo
            tvLatMeteo.setText(currentMeteo.getLatitudeMeteo());

            // set the longitude of current meteo
            tvLonMeteo.setText(currentMeteo.getLongitudeMeteo());

            // set the pressure of current meteo
            tvPressureMeteo.setText(currentMeteo.getPressureMeteo());

            // set the humidity of current meteo
            tvHumidityMeteo.setText(currentMeteo.getHumidityMeteo());

            // set the sea level of current meteo
            tvSeaLevelMeteo.setText(currentMeteo.getSeaLevelMeteo());

            // set the wind of current meteo
            tvWindMeteo.setText(currentMeteo.getWindSpeedMeteo());

            // set the wind degree of current meteo
            tvDegreeWindMeteo.setText(currentMeteo.getDegWindMeteo());
        }

        LinearLayout coord = (LinearLayout) findViewById(R.id.coordinateMeteoValue);
        coord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                latitude = tvLatMeteo.getText().toString();
                longitude = tvLonMeteo.getText().toString();

                String geoUri =
                        "https://www.windy.com/" + latitude + "/" + longitude + "?clouds," + latitude + "," + longitude + ",8";
                //"http://openweathermap.org/weathermap?basemap=map&cities=true&layer=precipitation&lat=" + latitude + "&lon=" + longitude + "&zoom=12";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));

                startActivity(intent);
            }
        });
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
