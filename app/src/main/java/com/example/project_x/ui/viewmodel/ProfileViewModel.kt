package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.project_x.common.Resource
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.data.model.FollowMessage
import com.example.project_x.data.model.FollowerResponse
import com.example.project_x.data.model.Post
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.pagination.UserPostsPagingSource
import com.example.project_x.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val userPostsPagingSource: UserPostsPagingSource,
) : ViewModel() {

  private val _profileState = MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading())
  val userProfileState: StateFlow<Resource<ProfileResponse>> = _profileState.asStateFlow()

  private val _userPosts = MutableStateFlow<PagingData<Post>>(PagingData.empty())
  val userPosts: StateFlow<PagingData<Post>> = _userPosts.asStateFlow()

  private val _loggedInUserProfileState =
    MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading())
  val loggedInUserProfileState: StateFlow<Resource<ProfileResponse>> =
    _loggedInUserProfileState.asStateFlow()

  private val _followState = MutableStateFlow<Resource<FollowMessage>>(Resource.Loading())

  private val _isFollowing = MutableStateFlow(false)
  val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

  private var isProfileFetched = false

  private val _followers = MutableStateFlow<Resource<FollowerResponse>>(Resource.Loading())
  val followers: StateFlow<Resource<FollowerResponse>> = _followers.asStateFlow()

  init {
    fetchUserProfile()
    getUserPosts()
  }

  private fun fetchUserProfile() {
    if (!isProfileFetched) {
      viewModelScope.launch {
        userRepository.getUserProfile().collect { resource ->
          _loggedInUserProfileState.value = resource
          isProfileFetched = true
        }
      }
    }
  }

  fun fetchUserProfileById(userId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.getUserProfileById(userId).collect { resource ->
        _profileState.value = resource
      }
    }
  }

  fun editProfile(id: String, user: EditProfileRequest) {
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.editProfile(id, user).collect { resource -> _profileState.value = resource }
    }
  }

  fun checkIfFollowing(userId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.isFollowingUser(userId).collect { resource ->
        if (resource is Resource.Success) {
          _isFollowing.value = resource.data?.isFollowing ?: false
        }
      }
    }
  }

  fun toggleFollowUser(userId: String) {
    // Toggle follow button state immediately
    _isFollowing.value = !_isFollowing.value

    // Update followers count locally
    _profileState.value =
      _profileState.value.data
        ?.let { profile ->
          val updatedFollowersCount =
            if (_isFollowing.value) {
              profile.user?.followersCount?.plus(1)
            } else {
              profile.user?.followersCount?.minus(1)
            }
          profile.copy(user = profile.user?.copy(followersCount = updatedFollowersCount))
        }
        ?.let { Resource.Success(it) } ?: _profileState.value

    // Perform the follow/unfollow operation in the background
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.followUser(userId).collect { resource ->
        _followState.value = resource
        if (resource is Resource.Error) {
          // If the API call fails, revert the changes
          _isFollowing.value = !_isFollowing.value
          _profileState.value =
            _profileState.value.data
              ?.let { profile ->
                val revertedFollowersCount =
                  if (_isFollowing.value) {
                    profile.user?.followersCount?.plus(1)
                  } else {
                    profile.user?.followersCount?.minus(1)
                  }
                profile.copy(user = profile.user?.copy(followersCount = revertedFollowersCount))
              }
              ?.let { Resource.Success(it) } ?: _profileState.value
        }
      }
    }
  }

  fun refreshProfile() {
    isProfileFetched = false // Reset flag to allow refresh
    fetchUserProfile()
  }

  fun getFollowers(userId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      userRepository.getFollowers(userId).collect { resource ->
        if (resource is Resource.Success) {
          _followers.value = resource
        }
      }
    }
  }

  private fun getUserPosts() {
    viewModelScope.launch {
      Pager(
          config =
            PagingConfig(
              pageSize = 30, // Larger page size to reduce the number of API calls
              enablePlaceholders = false,
              initialLoadSize = 30, // Load a larger initial page size
            )
        ) {
          userPostsPagingSource
        }
        .flow
        .cachedIn(viewModelScope)
        .collect { _userPosts.value = it }
    }
  }
}
