package it.flaviodepedis.meteoflax.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import it.flaviodepedis.meteoflax.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.flaviodepedis.meteoflax.model.Meteo;
import it.flaviodepedis.meteoflax.util.QueryUtils;

public class MeteoAdapter extends ArrayAdapter<Meteo> {

    /**
     * Constructs a new {@link MeteoAdapter}.
     *
     * @param context of the app
     * @param meteo is the list of meteos, which is the data source of the adapter
     */
    public MeteoAdapter(Context context, List<Meteo> meteo) {
        super(context, 0, meteo);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        ViewHolder holder;
        if (listItemView != null) {
            holder = (ViewHolder) listItemView.getTag();
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_meteo_list_item, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }

        // Find the meteo at the given position in the list of meteos
        Meteo currentMeteo = getItem(position);

        // Get the appropriate formatted Time and Date based on the current meteo
        String formattedDateTime[] = formatDateTime(currentMeteo.getTimeInMilliseconds());
        // Display the Date and Time of the current meteo in that TextView
        holder.tvDate.setText(formattedDateTime[0]);
        holder.tvTime.setText(formattedDateTime[1].substring(0,5));

        // Get the appropriate background based on the current meteo
        Drawable iconMeteo = QueryUtils.getBackgroundIcon(getContext(), currentMeteo.getIconMeteo());
        // Display the icon of the current meteo in that TextView
        holder.tvIcon.setBackground(iconMeteo);

        // Display the weather description of the current meteo in that TextView
        holder.tvWeatherDesc.setText(currentMeteo.getDescMeteo());

        // Get the appropriate temperature based on the current meteo
        String temperatureMeteo = formatTemperature(currentMeteo.getTempMeteo()) + "°";
        // Display the min temperature of the current meteo in that TextView
        holder.tvTemperature.setText(temperatureMeteo);

        return listItemView;
    }

    /**
     * Return the formatted date and time string from the object.
     */
    private String[] formatDateTime(String dateTimeObject) {
        // SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ITALIAN);
        String parts[] = dateTimeObject.split(" ");
        return parts;
    }

    /**
     * Return the formatted temperature string showing no decimal place (i.e. "3.2" => "3")
     */
    private String formatTemperature(String temperature) {
        if(temperature.contains(".")){
            int parts = temperature.indexOf(".");
            String ret = temperature.substring(0,parts+2);
            return ret;
        }
        else {
            return temperature;
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_date_value) TextView tvDate;
        @BindView(R.id.tv_time_value) TextView tvTime;
        @BindView(R.id.tv_icon_meteo) TextView tvIcon;
        @BindView(R.id.tv_weather_desc_value) TextView tvWeatherDesc;
        @BindView(R.id.tv_temperature_value) TextView tvTemperature;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
