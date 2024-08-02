package com.example.project_x.di

import android.content.Context
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.api.AuthInterceptor
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.datasource.CourseDataSource
import com.example.project_x.data.datasource.PostDataSource
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.pagination.PostPagingSource
import com.example.project_x.data.repository.PostRepository
import com.example.project_x.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.internal.Provider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

  @Singleton
  @Provides
  fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
    return TokenManager(context)
  }

  @Singleton
  @Provides
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
  }

  @Singleton
  @Provides
  fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
  }

  @Singleton
  @Provides
  fun provideRetrofitBuilder(): Retrofit.Builder {
    return Retrofit.Builder()
//                    .baseUrl("https://project-x-production-c8d8.up.railway.app/api/v1/")
      .baseUrl("http://192.168.1.7:3000/api/v1/")
      .addConverterFactory(GsonConverterFactory.create())
  }

  @Singleton
  @Provides
  fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService {
    return retrofitBuilder.build().create(ApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideAuthenticatedApiService(
    retrofitBuilder: Retrofit.Builder,
    okHttpClient: OkHttpClient,
  ): AuthenticatedApiService {
    return retrofitBuilder.client(okHttpClient).build().create(AuthenticatedApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
    return AuthInterceptor(tokenManager)
  }

  @Singleton
  @Provides
  fun provideUserDataSourceProvider(
    apiService: ApiService,
    authenticatedApiService: AuthenticatedApiService,
  ): Provider<UserDataSource> {
    return Provider { UserDataSource(apiService, authenticatedApiService) }
  }

  @Singleton
  @Provides
  fun provideDataSource(
    apiService: ApiService,
    authenticatedApiService: AuthenticatedApiService,
  ): UserDataSource {
    return UserDataSource(apiService, authenticatedApiService)
  }

  @Singleton
  @Provides
  fun provideCourseDataSource(authenticatedApiService: AuthenticatedApiService): CourseDataSource {
    return CourseDataSource(authenticatedApiService)
  }

  @Singleton
  @Provides
  fun providePostDataSource(authenticatedApiService: AuthenticatedApiService): PostDataSource {
    return PostDataSource(authenticatedApiService)
  }

  @Singleton
  @Provides
  fun providePostPagingSource(postRepository: PostRepository): PostPagingSource {
    return PostPagingSource(postRepository)
  }
}
