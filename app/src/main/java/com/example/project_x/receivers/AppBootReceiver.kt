package com.example.project_x.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.project_x.services.WebSocketService

class AppBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action || Intent.ACTION_MY_PACKAGE_REPLACED == intent.action) {
            // Start WebSocketService when the app is launched
            val serviceIntent = Intent(context, WebSocketService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}

