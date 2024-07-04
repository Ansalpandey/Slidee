package com.example.project_x.di

import android.content.Context
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
      .baseUrl("http://192.168.1.7:3000/api/v1/")
      .addConverterFactory(GsonConverterFactory.create())
      .client(
        OkHttpClient.Builder()
          .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
          )
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
  fun provideUserRepository(
    userDataSource: UserDataSource,
    @ApplicationContext context: Context,
  ): UserRepository {
    return UserRepository(userDataSource, context = context)
  }
}
