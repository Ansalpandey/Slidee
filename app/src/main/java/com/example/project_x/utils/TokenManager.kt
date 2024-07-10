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
}
