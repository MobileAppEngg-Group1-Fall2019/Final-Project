package com.group_01.finalproject

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.app.Service
import android.content.Context
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
import android.os.IBinder
import android.os.SystemClock


class WeatherService : Service {
    private lateinit var alarmIntent: PendingIntent
    var counter = 0

    constructor(applicationContext: Context) : super() {

        Log.i("SERVICE", "hService started")
    }

    constructor() {}

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        //instantiate your variables here

        //i call my startUpload method here to doing the task assigned to it

        startUpload()

        return START_STICKY
    }


    fun startUpload() {

        //call the method with your logic here

        //mine is a sample to print a log after every x seconds

        initializeTimerTask()

    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    fun initializeTimerTask() {
        // timerTask = new TimerTask() {
        var alarmMgr: AlarmManager? = null
        alarmMgr?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        )


        //we can print it on the logs as below
        Log.i("in timer", "in timer ++++  " + counter++)

        //or use the print statement as below
        println("Timer print " + counter++)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
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