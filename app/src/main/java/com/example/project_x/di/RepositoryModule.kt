package com.example.project_x.di

import com.example.project_x.data.implementation.CourseRepositoryImplementation
import com.example.project_x.data.implementation.UserRepositoryImplementation
import com.example.project_x.data.repository.CourseRepository
import com.example.project_x.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
  @Binds
  abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImplementation): UserRepository

  @Binds
  abstract fun bindCourseRepository(
    courseRepositoryImplementation: CourseRepositoryImplementation
  ): CourseRepository
}
