package com.example.movies.data.repository.search

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.local.search.SearchKeysEntity
import com.example.movies.data.remote.models.MoviePageListResponse

interface SearchLocalDataSource {

    fun searchMovies(queryString:String): PagingSource<Int, MovieEntity>
    suspend fun getSearchKeysForMoviesID(movieId:Long): SearchKeysEntity?
    @Transaction
    suspend fun cacheResponse(response: MoviePageListResponse,pageKey:Int,isRefresh:Boolean)

}