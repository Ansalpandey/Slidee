package com.example.project_x.websocket

import com.example.project_x.data.model.NotificationResponse
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject

class WebSocketManager @Inject constructor() {
  private lateinit var socket: Socket
  private val notificationChannel = Channel<NotificationResponse>(Channel.UNLIMITED)
  private var userId: String = ""

  init {
    initializeSocket()
  }

  private fun initializeSocket() {
    try {
      socket = IO.socket("http://10.0.2.2:5080")
    } catch (e: URISyntaxException) {
      e.printStackTrace()
    }
  }

  fun setUserId(userId: String) {
    this.userId = userId
  }

  fun connect() {
    socket.connect()

    socket.on(Socket.EVENT_CONNECT) { socket.emit("identify", userId) }
    socket.on(Socket.EVENT_DISCONNECT) {}

    // Listen for incoming notifications
    socket.on("new_notification") { args ->
      if (args.isNotEmpty() && args[0] is JSONObject) {
        val notificationJson = args[0] as JSONObject

        try {
          val notification =
            Gson().fromJson(notificationJson.toString(), NotificationResponse::class.java)
          // Send the notification to the channel
          notificationChannel.trySend(notification).isSuccess
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }
  }

  fun disconnect() {
    socket.disconnect()
  }

  // Flow for incoming notifications
  val incomingNotifications: Flow<NotificationResponse> = notificationChannel.receiveAsFlow()
}
