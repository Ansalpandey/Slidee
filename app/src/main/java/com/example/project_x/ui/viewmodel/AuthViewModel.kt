package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.implementation.UserRepositoryImplementation
import com.example.project_x.data.model.UserRequest
import com.example.project_x.data.model.UserResponse
import com.example.project_x.ui.stateholder.UserStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepositoryImplementation) :
  ViewModel() {

  private val _userStateHolder = MutableStateFlow(UserStateHolder())
  val userStateHolder: StateFlow<UserStateHolder> = _userStateHolder.asStateFlow()

  init {
    viewModelScope.launch {
      userRepository.userStateHolder.collect { state -> _userStateHolder.value = state }
    }
  }

  fun registerUser(user: UserRequest) {
    viewModelScope.launch {
      userRepository.registerUser(user).collect { resource ->
        handleResource(resource)
        if (resource is Resource.Success) {
          // If registration is successful, set isRegistered to true
          _userStateHolder.value = _userStateHolder.value.copy(isRegistered = true)
        }
      }
    }
  }

  fun loginUser(user: UserRequest) {
    viewModelScope.launch {
      userRepository.loginUser(user).collect { resource -> handleResource(resource) }
    }
  }

  private fun handleResource(resource: Resource<UserResponse>) {
    when (resource) {
      is Resource.Loading -> {
        _userStateHolder.value = _userStateHolder.value.copy(isLoading = true)
      }
      is Resource.Success -> {
        _userStateHolder.value =
          _userStateHolder.value.copy(
            isLoading = false,
            data = flowOf(resource.data!!),
            isLoggedIn = true,
            isRegistered = true, // Set registration status here
          )
      }
      is Resource.Error -> {
        _userStateHolder.value =
          _userStateHolder.value.copy(
            isLoading = false,
            error = resource.message,
            isRegistered = false, // Reset registration status on error
          )
      }
    }
  }

  fun resetUserState() {
    _userStateHolder.value = UserStateHolder()
  }

  fun logoutUser() {
    viewModelScope.launch {
      userRepository.logoutUser()
      _userStateHolder.value = UserStateHolder() // Reset the state holder after logout
    }
  }
}
