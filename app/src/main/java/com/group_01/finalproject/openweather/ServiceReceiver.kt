package com.group_01.finalproject.openweather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.group_01.finalproject.NotificationService

class ServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, NotificationService::class.java))
    }
}