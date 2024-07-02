package com.example.project_x.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.model.User
import com.example.project_x.preferences.UserPreferences
import com.example.project_x.preferences.dataStore
import com.example.project_x.ui.stateholder.UserStateHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

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
                password = "", // Password is not stored here for security reasons
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
      }
    }
  }

  suspend fun loginUser(user: User): Flow<Resource<User>> {
    return userDataSource.loginUser(user).onEach { resource ->
      if (resource is Resource.Success && resource.data != null) {
        setUserPreferences(resource.data, true)
      }
    }
  }

  private suspend fun setUserPreferences(user: User, isLoggedIn: Boolean) {
    dataStore.edit { preferences ->
      preferences[UserPreferences.IS_LOGGED_IN] = isLoggedIn
      preferences[UserPreferences.USER_NAME] = user.name ?: ""
      preferences[UserPreferences.USER_EMAIL] = user.email ?: ""
      preferences[UserPreferences.USER_AGE] = user.age ?: 0
      preferences[UserPreferences.USER_USERNAME] = user.username ?: ""
      preferences[UserPreferences.USER_BIO] = user.bio ?: ""
    }
  }

  suspend fun logoutUser() {
    dataStore.edit { preferences ->
      preferences[UserPreferences.IS_LOGGED_IN] = false
      preferences[UserPreferences.USER_NAME] = ""
      preferences[UserPreferences.USER_EMAIL] = ""
      preferences[UserPreferences.USER_AGE] = 0
      preferences[UserPreferences.USER_USERNAME] = ""
      preferences[UserPreferences.USER_BIO] = ""
    }
  }
}
