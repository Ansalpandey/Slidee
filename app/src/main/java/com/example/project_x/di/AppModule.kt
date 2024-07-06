package com.example.project_x.di

import android.content.Context
import com.example.project_x.data.api.ApiService
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.datasource.UserDataSource
import com.example.project_x.data.repository.UserRepository
import com.example.project_x.utils.AuthInterceptor
import com.example.project_x.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Singleton
  @Provides
  fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
    return TokenManager(context)
  }

  @Singleton
  @Provides
  fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
    return AuthInterceptor(tokenManager)
  }

  @Singleton
  @Provides
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
  }

  @Singleton
  @Provides
  fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(authInterceptor)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  @Singleton
  @Provides
  fun provideRetrofitBuilder(): Retrofit.Builder {
    return Retrofit.Builder()
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
    okHttpClient: OkHttpClient
  ): AuthenticatedApiService {
    return retrofitBuilder
      .client(okHttpClient)
      .build()
      .create(AuthenticatedApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideDataSource(
    apiService: ApiService,
    authenticatedApiService: AuthenticatedApiService
  ): UserDataSource {
    return UserDataSource(apiService, authenticatedApiService)
  }

  @Singleton
  @Provides
  fun provideUserRepository(
    userDataSource: UserDataSource,
    tokenManager: TokenManager, @ApplicationContext context: Context
  ): UserRepository {
    return UserRepository(userDataSource, context = context, tokenManager = tokenManager)
  }
}
