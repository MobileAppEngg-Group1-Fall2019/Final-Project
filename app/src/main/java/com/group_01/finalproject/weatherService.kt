package com.group_01.finalproject

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.icu.util.ULocale.getCountry
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.UserModel
import com.kwabenaberko.openweathermaplib.constants.Lang
import com.kwabenaberko.openweathermaplib.constants.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback



class WeatherIntentService : IntentService("WeatherIntentService") {

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    override fun onHandleIntent(intent: Intent?) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        Log.d("### Service Test", "Service running")
        var dbHelper = DBInterface(this)
        val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        weatherHelper.setUnits(Units.IMPERIAL)
        weatherHelper.setLang(Lang.ENGLISH)
        val TAG = "### Weather API"
        var user: UserModel = dbHelper.getUser(1)

        weatherHelper.getCurrentWeatherByGeoCoordinates(user.lat, user.long, object : CurrentWeatherCallback {
            override fun onSuccess(currentWeather: CurrentWeather) {
                Log.v(
                    TAG,
                    "Coordinates: " + currentWeather.coord.lat + ", " + currentWeather.coord.lon + "\n"
                            + "Weather Description: " + currentWeather.weather[0].description + "\n"
                            + "Temperature: " + currentWeather.main.tempMax + "\n"
                            + "Wind Speed: " + currentWeather.wind.speed + "\n"
                            + "City, Country: " + currentWeather.name + ", " + currentWeather.sys.country
                )
            }

            override fun onFailure(throwable: Throwable) {
                Log.v(TAG, throwable.message)
            }
        })
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            // Restore interrupt status.
            Thread.currentThread().interrupt()
        }

    }
}