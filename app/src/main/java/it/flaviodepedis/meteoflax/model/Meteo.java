package it.flaviodepedis.meteoflax.model;

import java.io.Serializable;

/**
 * Created by flavio.depedis on 12/05/2017.
 */
public class Meteo implements Serializable {


    /** Time of the meteo */
    private String mTimeInMilliseconds;

    /** Temperature of the meteo */
    private String mTempMeteo;

    /** Min temperature of the meteo */
    private String mMinTempMeteo;

    /** Max temperature of the meteo */
    private String mMaxTempMeteo;

    /** Description of the meteo */
    private String mDescMeteo;

    /** Icon of the current hour meteo */
    private String mIconMeteo;

    /** Current value of the meteo wind speed */
    private String mWindSpeedMeteo;

    /** Name of city */
    private String mCityMeteo;

    /** Name of country of the city */
    private String mCountryMeteo;

    /** Latitude of the city */
    private String mLatitudeMeteo;

    /** Longitude of the city */
    private String mLongitudeMeteo;

    /** Pressure of the city */
    private String mPressureMeteo;

    /** Sea Level of the city */
    private String mSeaLevelMeteo;

    /** Wind Degree of the city */
    private String mDegWindMeteo;

    /** Humidity of the city */
    private String mHumidityMeteo;


    /**
     * Constructs a new {@link Meteo} object.
     */
    public Meteo(String mTimeInMilliseconds, String mTempMeteo, String mMinTempMeteo,
                 String mMaxTempMeteo, String mDescMeteo, String mIconMeteo, String mWindSpeedMeteo,
                 String mCityMeteo, String mCountryMeteo, String mLatitudeMeteo, String mLongitudeMeteo,
                 String mPressureMeteo, String mSeaLevelMeteo, String mDegWindMeteo, String mHumidityMeteo ) {

        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.mTempMeteo = mTempMeteo;
        this.mMinTempMeteo = mMinTempMeteo;
        this.mMaxTempMeteo = mMaxTempMeteo;
        this.mDescMeteo = mDescMeteo;
        this.mIconMeteo = mIconMeteo;
        this.mWindSpeedMeteo = mWindSpeedMeteo;
        this.mCityMeteo = mCityMeteo;
        this.mCountryMeteo = mCountryMeteo;
        this.mLatitudeMeteo = mLatitudeMeteo;
        this.mLongitudeMeteo = mLongitudeMeteo;
        this.mPressureMeteo = mPressureMeteo;
        this.mSeaLevelMeteo = mSeaLevelMeteo;
        this.mDegWindMeteo = mDegWindMeteo;
        this.mHumidityMeteo = mHumidityMeteo;
    }

    /**
     * Returns the time of the meteo.
     */
    public String getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    /**
     * Returns the temperature of the meteo.
     */
    public String getTempMeteo() {
        return mTempMeteo;
    }

    /**
     * Returns the min temperature of the meteo.
     */
    public String getMinTempMeteo() {
        return mMinTempMeteo;
    }

    /**
     * Returns the max temperature of the meteo.
     */
    public String getMaxTempMeteo() {
        return mMaxTempMeteo;
    }

    /**
     * Returns the description of the meteo.
     */
    public String getDescMeteo() {
        return mDescMeteo;
    }

    /**
     * Returns the icon of the current hour meteo.
     */
    public String getIconMeteo() {
        return mIconMeteo;
    }

    /**
     * Returns the current value of the wind speed meteo.
     */
    public String getWindSpeedMeteo() {
        return mWindSpeedMeteo;
    }

    /**
     * Returns the current value of the city.
     */
    public String getCityMeteo() {
        return mCityMeteo;
    }

    /**
     * Returns the current value of the country.
     */
    public String getCountryMeteo() {
        return mCountryMeteo;
    }

    /**
     * Returns the current value of the latitude of the city.
     */
    public String getLatitudeMeteo() {
        return mLatitudeMeteo;
    }

    /**
     * Returns the current value of the longitude of the city.
     */
    public String getLongitudeMeteo() {
        return mLongitudeMeteo;
    }

    /**
     * Returns the current value of the pressure meteo of the city.
     */
    public String getPressureMeteo() {
        return mPressureMeteo;
    }

    /**
     * Returns the current value of the sea level of the city.
     */
    public String getSeaLevelMeteo() {
        return mSeaLevelMeteo;
    }

    /**
     * Returns the current value of the wind degree of the meteo of the city.
     */
    public String getDegWindMeteo() {
        return mDegWindMeteo;
    }

    /**
     * Returns the current value of the humidity of the city.
     */
    public String getHumidityMeteo() {
        return mHumidityMeteo;
    }
}
