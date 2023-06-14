package com.example.movies.data.repository.search

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.movies.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.map
import retrofit2.http.Query
import javax.inject.Inject

class SearchRepository @Inject constructor(val localDS:SearchLocalDataSource,val remoteDS:SearchRemoteDataSource) {

    @OptIn(ExperimentalPagingApi::class)
    fun search(query: String) = Pager(

        config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = {localDS.searchMovies(query.toSearchQuery())},
        remoteMediator = SearchMediator(query,localDS,remoteDS)
    ).flow.map {
            pagingData->
        pagingData.map {
            Log.d("TAG", "searchTitle : ${it.toMovie().title} ")
            it.toMovie()
        }
    }

    companion object {
        fun String.toSearchQuery() = "%${this.replace(' ', '%')}%"
    }
}