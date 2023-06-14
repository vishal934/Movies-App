package com.example.movies.data.remote.search

import com.example.movies.data.remote.models.MoviePageListResponse
import com.example.movies.data.repository.search.SearchRemoteDataSource
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService :SearchRemoteDataSource {


    @GET("search/movie?sort_by=popularity.desc" +
            "")
    override suspend fun searchmovie(@Query("query")query: String,@Query("page") page: Int): MoviePageListResponse



}