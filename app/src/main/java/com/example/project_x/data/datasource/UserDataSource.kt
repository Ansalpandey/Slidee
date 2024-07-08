package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource
@Inject
constructor(
  private val apiService: ApiService,
  private val authenticatedApiService: AuthenticatedApiService,
) {

  suspend fun registerUser(user: UserRequest): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.registerUser(user)
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Registration failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun loginUser(user: UserRequest): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.loginUser(user)
      if (response.isSuccessful) {
        val userResponse = response.body()
        emit(Resource.Success(userResponse))
      } else {
        emit(Resource.Error("Login failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun logoutUser(): Flow<Resource<UserResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.logoutUser()
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Logout failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun getUserProfile(): Flow<Resource<ProfileResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.getUserProfile()
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to fetch user profile: Empty response body")) }
      } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        emit(Resource.Error("Failed to fetch user profile: $errorMessage"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }
}
