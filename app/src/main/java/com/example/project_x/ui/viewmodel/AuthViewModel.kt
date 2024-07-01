package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.data.model.User
import com.example.project_x.data.repository.UserRepository
import com.example.project_x.ui.stateholder.UserStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
  private val _userStateHolder = MutableStateFlow(UserStateHolder())
  val userStateHolder = _userStateHolder

  private val _loggedInUserStateHolder = MutableStateFlow(UserStateHolder())
  val loggedInUserStateHolder = _loggedInUserStateHolder

  fun registerUser(user: User) {
    viewModelScope.launch {
      try {
        userStateHolder.value = userStateHolder.value.copy(isLoading = true)
        val result = userRepository.registerUser(user)
        userStateHolder.value = userStateHolder.value.copy(isLoading = false, data = result)
      } catch (e: Exception) {
        userStateHolder.value =
          userStateHolder.value.copy(isLoading = false, error = e.message ?: "An error occurred")
      }
    }
  }

  fun loginUser(user: User) {
    viewModelScope.launch {
      try {
        loggedInUserStateHolder.value = loggedInUserStateHolder.value.copy(isLoading = true)
        val result = userRepository.loginUser(user)
        loggedInUserStateHolder.value =
          loggedInUserStateHolder.value.copy(isLoading = false, data = result, isLoggedIn = true)
      } catch (e: Exception) {
        loggedInUserStateHolder.value =
          loggedInUserStateHolder.value.copy(
            isLoading = false,
            error = e.message ?: "An error occurred",
          )
      }
    }
  }
}
