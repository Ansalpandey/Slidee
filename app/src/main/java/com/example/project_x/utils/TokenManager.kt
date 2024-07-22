package com.example.project_x.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
  private val sharedPreferences = context.getSharedPreferences("TokenManager", Context.MODE_PRIVATE)

  fun saveToken(token: String) {
    sharedPreferences.edit().putString("token", token).apply()
  }

  fun getToken(): String? {
    return sharedPreferences.getString("token", null)
  }

  fun deleteToken() {
    sharedPreferences.edit().remove("token").apply()
  }

  fun saveRefreshToken(refreshToken: String) {
    sharedPreferences.edit().putString("refreshToken", refreshToken).apply()
  }

  fun getRefreshToken(): String? {
    return sharedPreferences.getString("refreshToken", null)
  }

  fun deleteRefreshToken() {
    sharedPreferences.edit().remove("refreshToken").apply()
  }

  fun clearTokens() {
    sharedPreferences.edit().clear().apply()
  }
}
