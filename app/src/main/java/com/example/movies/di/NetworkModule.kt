package com.example.movies.di

import com.example.movies.BuildConfig
import com.example.movies.data.remote.API_BASE_URL
import com.example.movies.data.remote.MovieService
import com.example.movies.data.remote.popular.PopularMovieService
import com.example.movies.data.remote.search.SearchService
import com.example.movies.data.repository.movies.MovieRemoteDataSource
import com.example.movies.data.repository.popular.PopularMoviesRemoteDataSource
import com.example.movies.data.repository.search.SearchRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val apiKeyInterceptor: (Interceptor.Chain) -> Response = { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val newUrl: HttpUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY).build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level= HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .addInterceptor(apiKeyInterceptor).build())
            .addConverterFactory(GsonConverterFactory.create()).build()

    }
    @Provides
    @Singleton
    fun providePopularMovieRemoteSource(retrofit: Retrofit):PopularMoviesRemoteDataSource=
        retrofit.create(PopularMovieService::class.java)

    @Provides
    @Singleton
    fun provideMovieDetailRemoteSource(retrofit: Retrofit):MovieRemoteDataSource=
        retrofit.create(MovieService::class.java)
    @Provides
    @Singleton
    fun SearchMovieRemoteSource(retrofit: Retrofit):SearchRemoteDataSource=
        retrofit.create(SearchService::class.java)
}