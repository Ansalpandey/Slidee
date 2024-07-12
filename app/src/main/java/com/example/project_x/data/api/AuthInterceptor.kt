package com.example.project_x.data.api

import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.utils.TokenManager
import dagger.internal.Provider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {
  @Inject
  lateinit var userDataSource: Provider<UserDataSource>

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
    val token = tokenManager.getToken()
    if (token != null) {
      request.addHeader("Authorization", "Bearer $token")
    }

    val response = chain.proceed(request.build())

    if (response.code == 401) {
      // Token expired, try to refresh
      synchronized(this) {
        val newToken = refreshAccessToken()
        if (newToken != null) {
          // Retry the original request with the new token
          val newRequest =
            chain.request().newBuilder().header("Authorization", "Bearer $newToken").build()
          response.close()
          return chain.proceed(newRequest)
        }
      }
    }

    return response
  }

  private fun refreshAccessToken(): String? {
    val refreshToken = tokenManager.getRefreshToken() ?: return null

    return try {
      val refreshResponse = runBlocking {
        userDataSource.get().refreshToken(refreshToken)
      } // Use value
      if (refreshResponse is Resource.Success) {
        val newToken = refreshResponse.data?.token
        if (newToken != null) {
          tokenManager.saveToken(newToken)
        }
        newToken
      } else {
        tokenManager.clearTokens()
        null
      }
    } catch (e: Exception) {
      e.printStackTrace()
      tokenManager.clearTokens()
      null
    }
  }
}
