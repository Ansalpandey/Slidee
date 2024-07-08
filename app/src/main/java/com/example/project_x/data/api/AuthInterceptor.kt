package com.example.project_x.data.api

import android.util.Log
import com.example.project_x.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
    val token = tokenManager.getToken()
      if (token != null) {
          request.addHeader("Authorization", "Bearer $token")
      }
    Log.d("AuthInterceptor", "intercept: $token")
    return chain.proceed(request.build())
  }
}
