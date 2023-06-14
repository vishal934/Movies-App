package com.example.movies.data.repository.search

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.local.search.SearchKeysEntity
import com.example.movies.data.remote.STARTING_PAGE_INDEX
import com.example.movies.domain.utils.NetworkError
import com.example.movies.domain.utils.toIIError
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SearchMediator(private val query :String,
                     private val localDS:SearchLocalDataSource,
                     private val remoteDS:SearchRemoteDataSource
                     ):RemoteMediator<Int,MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
     val pageKey= when(loadType){
         LoadType.REFRESH ->{
             val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
             remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
         }
         LoadType.PREPEND->{
             val remoteKey = getRemoteKeyForFirstItem(state)
             remoteKey?.preKey
                 ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
         }
         LoadType.APPEND->{
             val remoteKey = getRemoteKeyForLastItem(state)
             remoteKey?.nextKey
                 ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
         }
     }
        return try {
            val api=remoteDS.searchmovie(query,pageKey)
            Log.d("apiresponse", api.toString())
            localDS.cacheResponse(api,pageKey,loadType== LoadType.REFRESH)
            return  MediatorResult.Success(endOfPaginationReached =  pageKey >= api.total_pages)
        } catch (e: HttpException) {

             MediatorResult.Error(e.toIIError())
        } catch (e: Exception) {
            MediatorResult.Error(e.toIIError())
        }
    }


    private suspend fun getRemoteKeyForLastItem(state:PagingState<Int,MovieEntity>):SearchKeysEntity?{
      return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie->localDS.getSearchKeysForMoviesID(movie.id) }
    }
    private suspend fun getRemoteKeyForFirstItem(state:PagingState<Int,MovieEntity>):SearchKeysEntity?{
      return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie->localDS.getSearchKeysForMoviesID(movie.id) }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state:PagingState<Int,MovieEntity>):SearchKeysEntity?{
      return state.anchorPosition?.let { position ->
          state.closestItemToPosition(position)?.id?.let {  movieId -> localDS.getSearchKeysForMoviesID(movieId) }
      }
    }


}