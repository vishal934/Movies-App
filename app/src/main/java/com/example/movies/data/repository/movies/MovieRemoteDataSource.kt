package com.example.movies.data.repository.movies

import com.example.movies.data.remote.models.MovieResponse

interface MovieRemoteDataSource {
    suspend fun getMovie(id:Long): MovieResponse
}