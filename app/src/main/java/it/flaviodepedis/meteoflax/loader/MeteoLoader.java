package it.flaviodepedis.meteoflax.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;

import it.flaviodepedis.meteoflax.model.Meteo;
import it.flaviodepedis.meteoflax.util.QueryUtils;

/**
 * Created by flavio.depedis on 12/05/2017.
 */

public class MeteoLoader extends AsyncTaskLoader<List<Meteo>> {

    /** Tag for log messages */
    private static final String LOG_TAG = MeteoLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link MeteoLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public MeteoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        Log.w(LOG_TAG, "Log - in onStartLoading() method");

        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Meteo> loadInBackground() {

        Log.i(LOG_TAG, "Log - in loadInBackground() method");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of meteos.
        List<Meteo> meteos = QueryUtils.fetchMeteoData(mUrl, getContext());
        return meteos;
    }
}
