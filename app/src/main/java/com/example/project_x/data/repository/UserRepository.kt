package com.example.project_x.data.repository

import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.model.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository @Inject constructor(private val userDataSource: UserDataSource) {
  fun registerUser(user: User): Flow<User> {
    return userDataSource.registerUser(user).map { result -> result.getOrElse { throw it } }
  }

  fun loginUser(user: User): Flow<User> {
    return userDataSource.loginUser(user).map { result -> result.getOrElse { throw it } }
  }
}
