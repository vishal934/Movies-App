package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.data.local.MovieDatabase
import com.example.movies.data.repository.movies.MovieLocalDataSource
import com.example.movies.data.repository.popular.PopularMoviesLocalDataSource
import com.example.movies.data.repository.search.SearchLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context):MovieDatabase{
        return Room.databaseBuilder(context,MovieDatabase::class.java,"Movies.db").build()
    }
    @Provides
    @Singleton
    fun providePopularMovieDataSource(database:MovieDatabase):PopularMoviesLocalDataSource{
        return database.popularMoviesDao()
    }

    @Provides
    @Singleton
    fun provideDetailMovieDataSource(database:MovieDatabase):MovieLocalDataSource{
        return database.movieDetailDao()
    }
    @Provides
    @Singleton
    fun searchMovieDataSource(database:MovieDatabase): SearchLocalDataSource {
        return database.searchDetailDao()
    }
}