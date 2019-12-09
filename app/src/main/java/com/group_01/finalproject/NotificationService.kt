package com.group_01.finalproject


import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.group_01.finalproject.db.DBInterface
import java.util.*
import kotlin.collections.ArrayList

class NotificationService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d("### Service Test", "Notification service running")
        var dbHelper = DBInterface(this)
        val curTime: Date = Calendar.getInstance().time

        val TAG = "##Notification Service"
        var plants = dbHelper.getAllPlants()

        // map of plant to water frequency
        val map = mapOf("Tomato" to 2, "Cactus" to 5 , "Peppers" to 4)

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
            val notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.forget_me_not)
                .setContentTitle("Time to water your plants!")
                .setContentText("Water: $plantsToWater")
                .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notification)
        }

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
}


/*
class NotificationService : IntentService("NotificationService") {

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    override fun onHandleIntent(intent: Intent?) {
        Log.d("### Service Test", "Notification service running")
        var dbHelper = DBInterface(this)
        val curTime: Date = Calendar.getInstance().time

        val TAG = "##Notification Service"
        var plants = dbHelper.getAllPlants()

        // map of plant to water frequency
        val map = mapOf("Tomato" to 2, "Succulent" to 5 , "Peppers" to 4)

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
            val notification = Notification.Builder(this)
                .setContentTitle("Time to water your plants!")
                .setContentText("Water: $plantsToWater")
                .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, notification)
        }

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
} */
