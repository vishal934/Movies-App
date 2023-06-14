package com.example.movies.data.remote

import com.example.movies.data.remote.models.MovieResponse
import com.example.movies.data.repository.movies.MovieRemoteDataSource
import retrofit2.http.GET

interface MovieService:MovieRemoteDataSource {
    @GET("movie/{id}")
    override suspend fun getMovie(id: Long): MovieResponse
}