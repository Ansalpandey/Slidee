package com.example.project_x.data.repository

import com.example.project_x.common.Resource
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import com.example.project_x.ui.stateholder.UserStateHolder
import kotlinx.coroutines.flow.Flow

interface UserRepository {
  val userStateHolder: Flow<UserStateHolder>

  suspend fun registerUser(user: UserRequest): Flow<Resource<UserResponse>>

  suspend fun loginUser(user: UserRequest): Flow<Resource<UserResponse>>

  suspend fun logoutUser()

  fun getUserProfile(): Flow<Resource<ProfileResponse>>
}
