package com.example.project_x.data.datasource

import com.example.project_x.data.api.ApiService
import com.example.project_x.data.model.User
import javax.inject.Inject

class UserDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun registerUser(user: User) = apiService.registerUser(user)
}
