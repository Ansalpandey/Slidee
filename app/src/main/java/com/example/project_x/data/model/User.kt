package com.example.project_x.data.model

data class User(
  val id: String? = "",
  val name: String? = null,
  val email: String? = null,
  val age: Int? = null,
  val username: String? = null,
  val profileImage: String? = null,
  val coverImage: String? = null,
  val bio: String? = null,
  val password: String? = null,
  var accessToken: String? = null,
)
