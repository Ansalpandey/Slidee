package com.example.project_x

import android.app.Application
import com.example.project_x.utils.TokenRefreshManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ProjectApplication : Application() {

  @Inject lateinit var tokenRefreshManager: TokenRefreshManager

  override fun onCreate() {
    super.onCreate()
    if (::tokenRefreshManager.isInitialized) {
      tokenRefreshManager.startTokenRefresh()
    }
  }
}
