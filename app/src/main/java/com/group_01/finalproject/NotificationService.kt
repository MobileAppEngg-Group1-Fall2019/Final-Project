package com.group_01.finalproject


import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.group_01.finalproject.db.DBInterface
import java.util.*

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

        val map = mapOf("Tomato" to 2, "Succulent" to 5 , "Peppers" to 4)

        val plantsToWater = ArrayList<String>()
        for (plant in plants) {
            var dateToWater = getWaterDate(plant.lastCare, plant.type, map)
            if (dateToWater == null) {
                Log.i(TAG, "Plant type ${plant.type} did not have a corresponding watering frequency.")
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

    fun getWaterDate(date: Date, type: String, map: Map<String, Int>): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val numDays: Int? = map[type]
        if (numDays != null) {
            // add number of days
            calendar.add(Calendar.DAY_OF_YEAR, numDays)
            val newDate = calendar.time
            return newDate
        }
        return null
    }
}