package com.example.movies.data.repository.popular

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.movies.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PopularMovieRepository  @Inject constructor(
    private val localDS:PopularMoviesLocalDataSource,
    private val remoteDS:PopularMoviesRemoteDataSource)
{

 @OptIn(ExperimentalPagingApi::class)
 fun getPopularMovies()= Pager(
     config = PagingConfig( pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
     remoteMediator = PopularMoviesPagerMediator(localDS,remoteDS),
     pagingSourceFactory = {
         localDS.getAll()
     }
 ).flow.map{pagingData ->
     pagingData.map{
         it.toMovie()
     }

 }
}