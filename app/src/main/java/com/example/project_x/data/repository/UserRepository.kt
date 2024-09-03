package com.example.project_x.data.repository

import com.example.project_x.common.Resource
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.data.model.FollowMessage
import com.example.project_x.data.model.FollowerResponse
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.SearchResponse
import com.example.project_x.data.model.TokenResponse
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import com.example.project_x.ui.stateholder.UserStateHolder
import kotlinx.coroutines.flow.Flow

interface UserRepository {
  val userStateHolder: Flow<UserStateHolder>

  suspend fun registerUser(user: UserRequest): Flow<Resource<UserResponse>>

  suspend fun loginUser(user: UserRequest): Flow<Resource<UserResponse>>

  suspend fun logoutUser()

  suspend fun getUserProfile(): Flow<Resource<ProfileResponse>>

  suspend fun getUserProfileById(id: String): Flow<Resource<ProfileResponse>>

  suspend fun followUser(id: String): Flow<Resource<FollowMessage>>

  suspend fun isFollowingUser(id: String): Flow<Resource<FollowMessage>>

  suspend fun refreshToken(): Resource<TokenResponse>

  suspend fun editProfile(id: String, user: EditProfileRequest): Flow<Resource<ProfileResponse>>

  suspend fun searchUsers(query: String): Flow<Resource<SearchResponse>>

  suspend fun getFollowers(id: String): Flow<Resource<FollowerResponse>>

  suspend fun getUserPosts(page: Int, pageSize: Int) : PostResponse
}
