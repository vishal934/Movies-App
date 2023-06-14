package com.example.movies.data.repository.search

import com.example.movies.data.remote.models.MoviePageListResponse

interface SearchRemoteDataSource {

    suspend fun searchmovie(query:String,page:Int):MoviePageListResponse


}