package com.example.movies.data.repository.popular

import com.example.movies.data.remote.models.MoviePageListResponse

interface PopularMoviesRemoteDataSource {
    suspend fun getPopularMovies(page:Int):MoviePageListResponse
}