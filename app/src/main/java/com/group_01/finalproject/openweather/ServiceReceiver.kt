package com.group_01.finalproject.openweather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyStartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Util.scheduleJob(context)
    }
}