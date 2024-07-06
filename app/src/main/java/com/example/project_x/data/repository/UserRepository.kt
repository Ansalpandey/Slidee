package com.example.project_x.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.User
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import com.example.project_x.preferences.UserPreferences
import com.example.project_x.preferences.dataStore
import com.example.project_x.ui.stateholder.UserStateHolder
import com.example.project_x.utils.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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
  private val tokenManager: TokenManager,
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
              UserResponse(
                message = "",
                user =
                User(
                  id = preferences[UserPreferences.USER_ID] ?: "",
                  name = preferences[UserPreferences.USER_NAME] ?: "",
                  email = preferences[UserPreferences.USER_EMAIL] ?: "",
                  age = preferences[UserPreferences.USER_AGE] ?: 0,
                  username = preferences[UserPreferences.USER_USERNAME] ?: "",
                  bio = preferences[UserPreferences.USER_BIO] ?: "",
                  profileImage = "",
                  coverImage = "",
                ),
              )
            ),
          error = "",
          isLoggedIn = isLoggedIn,
        )
      } else {
        UserStateHolder()
      }
    }

  suspend fun registerUser(user: UserRequest): Flow<Resource<UserResponse>> {
    return userDataSource.registerUser(user)
  }

  suspend fun loginUser(user: UserRequest): Flow<Resource<UserResponse>> {
    return userDataSource.loginUser(user).onEach { resource ->
      if (resource is Resource.Success) {
        setUserPreferences(resource.data!!, true)
        tokenManager.saveToken(resource.data.user?.token ?: "")
      }
    }
  }

  private suspend fun setUserPreferences(user: UserResponse, isLoggedIn: Boolean) {
    dataStore.edit { preferences ->
      preferences[UserPreferences.IS_LOGGED_IN] = isLoggedIn
      preferences[UserPreferences.USER_NAME] = user.user?.name ?: ""
      preferences[UserPreferences.USER_ID] = user.user?.id ?: ""
      preferences[UserPreferences.USER_EMAIL] = user.user?.email ?: ""
      preferences[UserPreferences.USER_AGE] = user.user?.age ?: 0
      preferences[UserPreferences.USER_USERNAME] = user.user?.username ?: ""
      preferences[UserPreferences.USER_BIO] = user.user?.bio ?: ""
      preferences[UserPreferences.COVER_IMAGE] = user.user?.coverImage ?: ""
      preferences[UserPreferences.PROFILE_IMAGE] = user.user?.profileImage ?: ""
    }
  }

  suspend fun logoutUser() {
    userDataSource.logoutUser()
    clearUserPreferences()
  }

  private suspend fun clearUserPreferences() {
    dataStore.edit { preferences -> preferences.clear() }
  }

  fun getUserProfile(): Flow<Resource<ProfileResponse>> = flow {
    userDataSource.getUserProfile().collect { resource ->
      Log.d("UserRepository", "getUserProfile: $resource")
      emit(resource)
    }
  }
}
