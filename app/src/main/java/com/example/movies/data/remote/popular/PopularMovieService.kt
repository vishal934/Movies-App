package com.example.movies.data.remote.popular

import com.example.movies.data.remote.models.MoviePageListResponse
import com.example.movies.data.repository.popular.PopularMoviesRemoteDataSource
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMovieService : PopularMoviesRemoteDataSource {

    @GET("discover/movie?sort_by=popularity.desc")
    override suspend fun getPopularMovies(@Query("page") page: Int): MoviePageListResponse
}