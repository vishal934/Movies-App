package com.example.movies.data.repository.movies

import com.example.movies.data.local.movie.MovieEntity

import kotlinx.coroutines.flow.Flow
interface MovieLocalDataSource {

    fun getMovieFlow(id: Long): Flow<MovieEntity?>
    suspend fun insert(movieEntity: MovieEntity)
}