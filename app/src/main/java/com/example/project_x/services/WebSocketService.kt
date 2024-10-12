package com.example.project_x.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.project_x.MainActivity
import com.example.project_x.R
import com.example.project_x.data.model.NotificationResponse
import com.example.project_x.preferences.UserPreferences
import com.example.project_x.preferences.dataStore
import com.example.project_x.websocket.WebSocketManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketService : LifecycleService() {
  @Inject lateinit var webSocketManager: WebSocketManager
  private val groupedNotifications = mutableListOf<NotificationResponse>()
  private lateinit var connectivityManager: ConnectivityManager
  private lateinit var networkCallback: ConnectivityManager.NetworkCallback
  private val coroutineScope = lifecycleScope // Use lifecycleScope for coroutines
  private var notificationTimer: Job? = null // To keep track of the coroutine job for timing
  private var loggedInUserId: String? = null // To hold the logged-in user ID

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
    startForeground(NOTIFICATION_ID, createNotification())

    // Initialize ConnectivityManager and NetworkCallback
    connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    networkCallback = createNetworkCallback()

    // Register the network callback
    connectivityManager.registerDefaultNetworkCallback(networkCallback)

    lifecycleScope.launch {
      loggedInUserId = getUserIdFromDataStore() // Retrieve user ID once at the start
      if (loggedInUserId != null) {
        webSocketManager.setUserId(loggedInUserId!!)
        webSocketManager.connect()

        // Listen for incoming notifications
        collectNotifications() // Start collecting notifications
      }
    }
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel =
        NotificationChannel(CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_LOW)
      val manager = getSystemService(NotificationManager::class.java)
      manager.createNotificationChannel(channel)
    }
  }

  private fun createNotification(): Notification {
    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("WebSocket Service")
      .setContentText("Listening for notifications...")
      .setSmallIcon(R.drawable.app_icon) // Use your app's notification icon
      .setContentIntent(pendingIntent)
      .build()
  }

  override fun onDestroy() {
    webSocketManager.disconnect()
    // Unregister the network callback
    connectivityManager.unregisterNetworkCallback(networkCallback)
    super.onDestroy()
  }

  private suspend fun getUserIdFromDataStore(): String? {
    val preferences = applicationContext.dataStore.data.first()
    return preferences[UserPreferences.USER_ID]
  }

  private suspend fun collectNotifications() {
    webSocketManager.incomingNotifications.collect { notification ->
      handleNotification(notification)
    }
  }

  private fun isConnectedToInternet(): Boolean {
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnected == true
  }

  private fun handleNotification(notification: NotificationResponse) {
    if (notification.user?._id == loggedInUserId) {
      return
    }

    if (isConnectedToInternet()) {
      showNotification(notification) // Send notification immediately
    }
  }

  private val sentNotificationIds = mutableSetOf<String>()

  private fun showNotification(notification: NotificationResponse) {
    // Skip if already sent
    if (sentNotificationIds.contains(notification._id)) return

    sentNotificationIds.add(notification._id!!)

    when {
      notification.type == "like_post" -> {
        groupedNotifications.add(notification)
        notificationTimer?.cancel()
        notificationTimer =
          coroutineScope.launch {
            delay(10000)
            if (groupedNotifications.size > 1) {
              sendGroupedNotifications()
            } else {
              // Send individual notification if the group is small
              sendIndividualNotification(notification)
            }
          }
      }
      notification.message?.length!! <= 1 -> {
        // Show individual notification for short messages
        sendIndividualNotification(notification)
      }
      else -> {
        // Handle other types of notifications (e.g., comments, messages) if necessary
        sendIndividualNotification(notification)
      }
    }
  }

  // Function to send an individual notification
  private fun sendIndividualNotification(notification: NotificationResponse) {
    val username = notification.user?.username ?: "Someone"
    val messageText = "$username ${notification.message}"

    val notificationBuilder =
      NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("New Notification")
        .setContentText(messageText)
        .setSmallIcon(R.drawable.app_icon)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = getSystemService(NotificationManager::class.java)
    notificationManager.notify(notification._id.hashCode(), notificationBuilder.build())
  }

  private fun sendGroupedNotifications() {
    val style =
      NotificationCompat.InboxStyle()
        .addLine("You have ${groupedNotifications.size} new notifications.")
    if (groupedNotifications.isNotEmpty()) {
      val summaryBuilder =
        NotificationCompat.Builder(this, CHANNEL_ID)
          .setContentTitle("New Notifications")
          .setContentText("You have ${groupedNotifications.size} new notifications.")
          .setSmallIcon(R.drawable.app_icon)
          .setStyle(style)
          .setGroup(GROUP_KEY_NOTIFICATIONS) // Set group key
          .setGroupSummary(true)

      val notificationManager = getSystemService(NotificationManager::class.java)
      notificationManager.notify(NOTIFICATION_ID, summaryBuilder.build())

      // Send individual notifications
      for (notification in groupedNotifications) {
        val individualBuilder =
          NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("New Notification")
            .setContentText("${notification.user?.username ?: "Someone"} ${notification.message}")
            .setSmallIcon(R.drawable.app_icon)
            .setGroup(GROUP_KEY_NOTIFICATIONS) // Set group key for individual notifications

        notificationManager.notify(notification._id.hashCode(), individualBuilder.build())
      }

      // Clear grouped notifications after showing
      groupedNotifications.clear()
    }
  }

  private var notificationsSent = false // Add this at the class level

  private fun createNetworkCallback(): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        if (!notificationsSent) {
          notificationsSent = true
        }
      }

      override fun onLost(network: Network) {
        notificationsSent = false
      }
    }
  }

  companion object {
    private const val CHANNEL_ID = "Slide_Notification_Channel"
    private const val NOTIFICATION_ID = 1
    private const val GROUP_KEY_NOTIFICATIONS = "grouped_notifications"
  }
}
