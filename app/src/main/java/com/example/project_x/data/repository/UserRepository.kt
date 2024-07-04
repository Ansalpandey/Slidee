package com.example.project_x.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.model.Profile
import com.example.project_x.data.model.User
import com.example.project_x.preferences.UserPreferences
import com.example.project_x.preferences.dataStore
import com.example.project_x.ui.stateholder.UserStateHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject
constructor(
  private val userDataSource: UserDataSource,
  @ApplicationContext private val context: Context,
) {
  private val dataStore = context.dataStore

  val userStateHolder: Flow<UserStateHolder> =
    dataStore.data.map { preferences ->
      val isLoggedIn = preferences[UserPreferences.IS_LOGGED_IN] ?: false
      if (isLoggedIn) {
        UserStateHolder(
          isLoading = false,
          data =
            flowOf(
              User(
                name = preferences[UserPreferences.USER_NAME],
                email = preferences[UserPreferences.USER_EMAIL],
                age = preferences[UserPreferences.USER_AGE],
                username = preferences[UserPreferences.USER_USERNAME],
                bio = preferences[UserPreferences.USER_BIO],
              )
            ),
          error = "",
          isLoggedIn = isLoggedIn,
        )
      } else {
        UserStateHolder()
      }
    }

  suspend fun registerUser(user: User): Flow<Resource<User>> {
    return userDataSource.registerUser(user).onEach { resource ->
      if (resource is Resource.Success && resource.data != null) {
        setUserPreferences(resource.data, true)
        setAccessToken(resource.data.accessToken ?: "")
      }
    }
  }

  suspend fun loginUser(user: User): Flow<Resource<User>> {
    return userDataSource.loginUser(user).onEach { resource ->
      if (resource is Resource.Success && resource.data != null) {
        setUserPreferences(resource.data, true)
        resource.data.accessToken?.let { setAccessToken(it) }
        Log.d("UserRepository", "loginUser: ${resource.data.accessToken}")
      }
    }
  }

  private suspend fun setUserPreferences(user: User, isLoggedIn: Boolean) {
    dataStore.edit { preferences ->
      preferences[UserPreferences.IS_LOGGED_IN] = isLoggedIn
      preferences[UserPreferences.USER_NAME] = user.name ?: ""
      preferences[UserPreferences.USER_ID] = user.id ?: ""
      preferences[UserPreferences.USER_EMAIL] = user.email ?: ""
      preferences[UserPreferences.USER_AGE] = user.age ?: 0
      preferences[UserPreferences.USER_USERNAME] = user.username ?: ""
      preferences[UserPreferences.USER_BIO] = user.bio ?: ""
    }
  }

  private suspend fun setAccessToken(accessToken: String) {
    dataStore.edit { preferences -> preferences[UserPreferences.ACCESS_TOKEN] = accessToken }
  }

  val ACCESS_TOKEN = stringPreferencesKey("access_token")

  val accessTokenFlow: Flow<String?> =
    dataStore.data.map { preferences -> preferences[ACCESS_TOKEN] }

  suspend fun logoutUser() {
    userDataSource.logoutUser()
    clearUserPreferences()
  }

  private suspend fun clearUserPreferences() {
    dataStore.edit { preferences -> preferences.clear() }
  }

  suspend fun getUserProfile(): Flow<Resource<Profile>> {
    val accessToken = accessTokenFlow.firstOrNull()
    return if (accessToken != null) {
      userDataSource.getUserProfile(accessToken).onEach { resource ->
        if (resource is Resource.Success && resource.data != null) {
          setUserPreferences(resource.data.user, true)
        }
      }
    } else {
      flow { emit(Resource.Error("No access token found")) }
    }
  }
}
