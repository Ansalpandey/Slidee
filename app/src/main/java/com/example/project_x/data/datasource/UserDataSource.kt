package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.model.Profile
import com.example.project_x.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource @Inject constructor(private val apiService: ApiService) {
  suspend fun registerUser(user: User): Flow<Resource<User>> = flow {
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

  suspend fun loginUser(user: User): Flow<Resource<User>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.loginUser(user)
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Login failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun logoutUser(): Flow<Resource<User>> = flow {
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

  suspend fun getUserProfile(accessToken: String): Flow<Resource<Profile>> = flow {
    emit(Resource.Loading())
    try {
      val response = apiService.getUserProfile(accessToken)
      if (response.isSuccessful) {
        emit(Resource.Success(response.body()))
      } else {
        emit(Resource.Error("Failed to load profile"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }
}
