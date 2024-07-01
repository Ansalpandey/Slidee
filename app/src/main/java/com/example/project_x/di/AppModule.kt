package com.example.project_x.di

import com.example.project_x.data.api.ApiService
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Provides
  fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://project-x-production-c8d8.up.railway.app/api/v1/")
      .addConverterFactory(GsonConverterFactory.create())
      .client(
        OkHttpClient.Builder()
          .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
          .build()
      )
      .build()
  }

  @Provides
  fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
  }

  @Provides
  fun provideDataSource(apiService: ApiService): UserDataSource {
    return UserDataSource(apiService)
  }

  @Provides
  fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
    return UserRepository(userDataSource)
  }
}
