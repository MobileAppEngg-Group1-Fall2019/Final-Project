package com.group_01.finalproject.openweather

import android.app.IntentService
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.UserModel
import com.kwabenaberko.openweathermaplib.constants.Lang
import com.kwabenaberko.openweathermaplib.constants.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback
import android.app.job.JobParameters
import android.app.AlarmManager.RTC_WAKEUP
import android.app.AlarmManager
import android.os.IBinder
import com.group_01.finalproject.R


class WeatherService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Query the database and show alarm if it applies
        // For actual api stuff, copy over weather intent service functionality - this is just here to show you it works
        Log.d("### Service Test", "Service running")
        val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        weatherHelper.setUnits(Units.IMPERIAL)
        weatherHelper.setLang(Lang.ENGLISH)
        var dbHelper = DBInterface(this)
        // val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        // weatherHelper.setUnits(Units.IMPERIAL)
        // weatherHelper.setLang(Lang.ENGLISH)
        val TAG = "### Weather API"
        var user: UserModel = dbHelper.getUser(1)
        var newUser: UserModel = UserModel(user.userId, user.name, user.points + 1, user.consistencyBadge, user.diversityBadge, user.photosBadge, user.greenThumbBadge, user.badgeOfBadges, user.creationDate, user.lat, user.long)
        dbHelper.updateUser(newUser)
        dbHelper.closeConnection()
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

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
        stopSelf()

        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        // I want to restart this service again in one hour
        val alarm: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 1000 * 60 * 60 /* To test, change this time to 10000 (every 10 seconds) */,
            PendingIntent.getService(this, 0, Intent(this, WeatherService::class.java), 0)
        )
    }
}

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
        // val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        // weatherHelper.setUnits(Units.IMPERIAL)
        // weatherHelper.setLang(Lang.ENGLISH)
        val TAG = "### Weather API"
        var user: UserModel = dbHelper.getUser(1)
        var newUser: UserModel = UserModel(user.userId, user.name, user.points + 1, user.consistencyBadge, user.diversityBadge, user.photosBadge, user.greenThumbBadge, user.badgeOfBadges, user.creationDate, user.lat, user.long)
        dbHelper.updateUser(newUser)
/*
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

 */


    }
}