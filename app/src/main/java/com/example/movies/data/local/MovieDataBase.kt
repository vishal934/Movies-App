package com.example.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.data.local.movie.MovieDao
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.local.popular.PopularMovieDao
import com.example.movies.data.local.popular.PopularMovieKeyEntity
import com.example.movies.data.local.search.SearchDao
import com.example.movies.data.local.search.SearchKeysEntity

@Database(entities = [MovieEntity::class,PopularMovieKeyEntity::class, SearchKeysEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun popularMoviesDao():PopularMovieDao
    abstract fun movieDetailDao():MovieDao
    abstract fun searchDetailDao(): SearchDao

}