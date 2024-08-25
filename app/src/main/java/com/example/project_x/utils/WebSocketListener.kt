package com.example.project_x.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketListener(private val onEvent: (String) -> Unit) : WebSocketListener() {

  override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
    // WebSocket connection opened
    Log.d("Websocket", "Web socket opened")
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    // Handle text messages
    onEvent(text)
  }

  override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
    // Handle binary messages if needed
  }

  override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    webSocket.close(1000, null)
    Log.d("Websocket", "Web socket closed with:- $reason")
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
    // Handle connection failure
    Log.d("Websocket", "Web socket failed with:- ${t.message}")
  }
}

fun createWebSocket(onEvent: (String) -> Unit): WebSocket {
  val client = OkHttpClient()
  val request =
    Request.Builder()
      .url("ws://192.168.1.7:4000") // Replace with your WebSocket server URL
      .build()

  return client.newWebSocket(request, WebSocketListener(onEvent))
}
