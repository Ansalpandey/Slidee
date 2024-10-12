package com.example.project_x.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.project_x.services.WebSocketService

class ConnectivityReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    context?.let {
      if (isConnected(it)) {
        // Start WebSocketService when connected
        Intent(context, WebSocketService::class.java).also { serviceIntent ->
          context.startForegroundService(serviceIntent)
        }
      }
    }
  }

  private fun isConnected(context: Context): Boolean {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnected == true
  }
}
