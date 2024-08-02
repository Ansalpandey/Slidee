plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
  id("com.google.gms.google-services")
  alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
  namespace = "com.example.project_x"
  compileSdk = 34

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
  composeOptions { kotlinCompilerExtensionVersion = "1.5.1" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
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

  implementation (libs.androidx.room.runtime)
  kapt (libs.androidx.room.compiler)
  implementation (libs.androidx.room.ktx)

  // Import the BoM for the Firebase platform
  implementation(platform(libs.firebase.bom))

  // Add the dependency for the Firebase Authentication library
  // When using the BoM, you don't specify versions in Firebase library dependencies
  implementation(libs.google.firebase.auth)

  // Also add the dependency for the Google Play services library and specify its version
  implementation(libs.play.services.auth)
}
