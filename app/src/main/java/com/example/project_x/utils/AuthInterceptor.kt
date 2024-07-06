package com.example.project_x.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

  @Inject
  lateinit var tokenManager: TokenManager
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
    val token = tokenManager.getToken()
    Log.d("AuthInterceptor", "intercept: $token")
    return chain.proceed(request.build())
  }
}
