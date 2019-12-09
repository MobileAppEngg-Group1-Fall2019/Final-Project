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
import android.R
import android.app.job.JobParameters


class TestJobService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        val service = Intent(applicationContext, WeatherIntentService::class.java)
        applicationContext.startService(service)
        Util.scheduleJob(applicationContext) // reschedule the job
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    companion object {
        private val TAG = "SyncService"
    }

}

class WeatherService : JobService {
    private lateinit var alarmIntent: PendingIntent
    var counter = 0

    constructor(applicationContext: Context) : super() {

        Log.i("SERVICE", "hService started")
    }

    constructor() {}

    private val TAG = "SyncService"

    override fun onStartJob(params: JobParameters): Boolean {
        val service = Intent(applicationContext, WeatherIntentService::class.java)
        applicationContext.startService(service)
        Util.scheduleJob(applicationContext) // reschedule the job
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
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