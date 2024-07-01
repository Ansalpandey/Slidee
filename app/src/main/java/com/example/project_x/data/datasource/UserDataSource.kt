package com.example.project_x.data.datasource

import com.example.project_x.data.api.ApiService
import com.example.project_x.data.model.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserDataSource @Inject constructor(private val apiService: ApiService) {
  fun registerUser(user: User): Flow<Result<User>> = flow {
    try {
      val response = apiService.registerUser(user)
      emit(Result.success(response))
    } catch (e: Exception) {
      emit(Result.failure<User>(e))
    }
  }

  fun loginUser(user: User): Flow<Result<User>> = flow {
    try {
      val response = apiService.loginUser(user)
      emit(Result.success(response))
    } catch (e: Exception) {
      emit(Result.failure(e))
    }
  }
}
