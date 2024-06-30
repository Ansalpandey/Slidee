package com.example.project_x.data.repository

import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.model.User
import javax.inject.Inject
class UserRepository @Inject constructor(private val userDataSource: UserDataSource) {
    suspend fun registerUser(user: User): User {
        return userDataSource.registerUser(user)
    }
}
