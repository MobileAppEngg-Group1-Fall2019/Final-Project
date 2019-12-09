package com.group_01.finalproject


import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.UserModel
import com.kwabenaberko.openweathermaplib.constants.Lang
import com.kwabenaberko.openweathermaplib.constants.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather
import java.util.*
import kotlin.collections.ArrayList


class NotificationService : Service() {
    val TAG = "##Notification Service"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d(TAG, "Notification service running")
        var dbHelper = DBInterface(this)
        val curTime: Date = Calendar.getInstance().time

        var plants = dbHelper.getAllPlants()

        // map of plant to water frequency
        val map = mapOf("Tomato" to 0, "Cactus" to 0 , "Peppers" to 0)

        Log.d("### Service Test", "Service running")
        val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        weatherHelper.setUnits(Units.IMPERIAL)
        weatherHelper.setLang(Lang.ENGLISH)

        var user: UserModel = dbHelper.getUser(1)
        var newUser: UserModel = UserModel(user.userId, user.name, user.points + 1, user.consistencyBadge, user.diversityBadge, user.photosBadge, user.greenThumbBadge, user.badgeOfBadges, user.creationDate, user.lat, user.long)
        dbHelper.updateUser(newUser)
        weatherHelper.getCurrentWeatherByGeoCoordinates(user.lat, user.long, object :
            CurrentWeatherCallback {
            override fun onSuccess(currentWeather: CurrentWeather) {
                Log.v(
                    TAG,
                    "Coordinates: " + currentWeather.coord.lat + ", " + currentWeather.coord.lon + "\n"
                            + "Weather Description: " + currentWeather.weather[0].description + "\n"
                            + "Temperature: " + currentWeather.main.tempMax + "\n"
                            + "Wind Speed: " + currentWeather.wind.speed + "\n"
                            + "City, Country: " + currentWeather.name + ", " + currentWeather.sys.country
                )

                checkTemperature(currentWeather.main.tempMax)
            }

            override fun onFailure(throwable: Throwable) {
                Log.v(TAG, throwable.message)
            }
        })

        val plantsToWater = ArrayList<String>()
        for (plant in plants) {
            var dateToWater = getWaterDate(plant.lastCare, plant.type, map)
            if (dateToWater == null) {
                Log.i(TAG, "Plant type ${plant.type} does not have a corresponding watering frequency.")
                continue
            }

            if(curTime > dateToWater) {
                Log.i(TAG,"Current Time occurs after Date Water")
                // Time to water
                plantsToWater.add(plant.name)
            } else if(curTime < dateToWater) {
                Log.i(TAG,"Current Time occurs before Date Water")
                // No need to water - do nothing
            } else {
                Log.i(TAG,"Both dates are equal")
                // Time to water
                plantsToWater.add(plant.name)
            }
        }

        if (plantsToWater.isEmpty()) {
            Log.i(TAG, "No plants need to be watered now")
        } else {
            Log.i(TAG, "Plants to be watered: $plantsToWater")
            // send notification

            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, MainActivity::class.java)
            // Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val notification = NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.notification_flower)
                .setContentTitle("Time to water your plants!")
                .setContentText("Water: $plantsToWater")
                .setContentIntent(resultPendingIntent)
                .build()

            // Removes notification after it is clicked.
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notification)
        }

        dbHelper.closeConnection()
        stopSelf()

        return START_NOT_STICKY
    }

    private fun getWaterDate(date: Date, type: String, map: Map<String, Int>): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val numDays: Int? = map[type]
        if (numDays != null) {
            // add number of days
            calendar.add(Calendar.DAY_OF_YEAR, numDays)
            return calendar.time
        }
        return null
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
            PendingIntent.getService(this, 0, Intent(this, NotificationService::class.java), 0)
        )
    }

    private fun checkTemperature(temperature: Double) {

        if (temperature < 50) {
            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, MainActivity::class.java)
            // Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val notification = NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.notification_flower)
                .setContentTitle("Warning: Move your outdoor plants inside")
                .setContentText("Temperature is $temperature, which is unsafe for outdoor plants.")
                .setContentIntent(resultPendingIntent)
                .build()

            // Removes notification after it is clicked.
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notification)
        }
    }
}

