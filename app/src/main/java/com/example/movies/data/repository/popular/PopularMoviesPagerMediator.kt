package com.example.movies.data.repository.popular

import android.graphics.Movie
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.local.popular.PopularMovieKeyEntity
import com.example.movies.data.remote.STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesPagerMediator(
    private val local:PopularMoviesLocalDataSource,
    private val remote:PopularMoviesRemoteDataSource
):RemoteMediator<Int,MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
     val pageKey = when(loadType){
         LoadType.REFRESH ->{
             val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
             remoteKey?.nextKey?.minus(1)?: STARTING_PAGE_INDEX

         }
         LoadType.PREPEND ->{
             val remoteKey=getRemoteKeyForFirstItem(state)
             remoteKey?.preKey?:return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
         }
         LoadType.APPEND->{
             val remoteKey=getRemoteKeyForLastItem(state)
             remoteKey?.nextKey?:return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
         }
     }
        return try {
            val apiresponse = remote.getPopularMovies(pageKey)
            local.cacheResponse(apiresponse,pageKey,loadType==LoadType.REFRESH)
            MediatorResult.Success(endOfPaginationReached = pageKey >= apiresponse.total_pages)
        }catch (exception:IOException){

            return MediatorResult.Error(exception)
        }  catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state:PagingState<Int,MovieEntity>):PopularMovieKeyEntity?
    {
        return state.anchorPosition?.let {position ->
            state.closestItemToPosition(position)
                ?.id?.let { movieId ->local.getRemoteKeysForMovieId(movieId) }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state:PagingState<Int,MovieEntity>):PopularMovieKeyEntity?{
        return state.pages.firstOrNull{it.data.isNotEmpty()}?.data?.firstOrNull()?.let { movie ->local.getRemoteKeysForMovieId(movie.id) }
    }

    private suspend fun getRemoteKeyForLastItem(state:PagingState<Int,MovieEntity>):PopularMovieKeyEntity?{
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let {
            movie->local.getRemoteKeysForMovieId(movie.id)
        }
    }
}