package com.example.movies.data.repository.popular

import androidx.paging.PagingSource
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.local.popular.PopularMovieKeyEntity
import com.example.movies.data.remote.models.MoviePageListResponse

interface PopularMoviesLocalDataSource {
    fun getAll(): PagingSource<Int, MovieEntity>
 suspend fun getRemoteKeysForMovieId(id: Long):PopularMovieKeyEntity?
    suspend fun cacheResponse(response: MoviePageListResponse,pageKey:Int,isRefresh:Boolean)
}