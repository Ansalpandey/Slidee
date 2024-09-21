plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
  namespace = "com.example.project_x"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.project_x"
    minSdk = 28
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions { jvmTarget = "1.8" }
  buildFeatures { compose = true }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildToolsVersion = "35.0.0"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.core.ktx)
  implementation(libs.androidx.ui.text.google.fonts)
  implementation(libs.androidx.material3.adaptive.navigation.suite.android)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // retrofit
  implementation(libs.retrofit)
  implementation(libs.converter.gson)
  implementation(libs.adapter.rxjava2)
  implementation(libs.logging.interceptor)

  // coil
  implementation(libs.coil.compose)

  // Dagger Hilt
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.androidx.hilt.navigation.compose)

  // ViewModel
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  // ViewModel utilities for Compose
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // LiveData
  implementation(libs.androidx.lifecycle.livedata.ktx)

  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.androidx.datastore.preferences)

  implementation(libs.androidx.core.splashscreen)

  implementation(libs.okhttp)

  // For rememberLauncherForActivityResult()
  implementation(libs.androidx.activity.compose.v161)

  // For PickVisualMedia contract
  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.material.icons.extended)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.paging.compose)

  implementation(libs.accompanist.pager)
  implementation(libs.accompanist.pager.indicators)

  implementation(libs.androidx.room.runtime)
  //noinspection KaptUsageInsteadOfKsp
  kapt(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)

  implementation(libs.accompanist.swiperefresh)

  // Exo
  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.ui)
  implementation(libs.androidx.media3.common)
  implementation(libs.androidx.media3.exoplayer.hls)

  implementation(libs.androidx.adaptive)
  implementation (libs.androidx.adaptive.layout)
  implementation (libs.androidx.adaptive.navigation)
  implementation ("com.airbnb.android:lottie-compose:6.3.0")
}
