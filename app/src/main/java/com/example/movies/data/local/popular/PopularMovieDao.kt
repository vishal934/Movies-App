package com.example.movies.data.local.popular

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.movies.data.local.CommonMovieListDao
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.remote.STARTING_PAGE_INDEX
import com.example.movies.data.remote.models.MoviePageListResponse
import com.example.movies.data.repository.popular.PopularMoviesLocalDataSource

@Dao
interface PopularMovieDao:PopularMoviesLocalDataSource,CommonMovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey:List<PopularMovieKeyEntity>)


    @Query("select * from popular_movies_keys where movieID=:id")
    override suspend fun getRemoteKeysForMovieId(id: Long): PopularMovieKeyEntity?

    @Query(
        """SELECT movies.* FROM movies
        INNER JOIN popular_movies_keys ON movies.id=popular_movies_keys.movieId
        ORDER BY curKey ASC, popularity DESC, title ASC"""
    )
    override fun getAll(): PagingSource<Int, MovieEntity>

    @Query("delete from  popular_movies_keys")
    suspend fun clearRemoteKeys()

    @Transaction
    override suspend fun cacheResponse(
        response: MoviePageListResponse,
        pageKey: Int,
        isRefresh: Boolean
    ) {
        if(isRefresh)
            clearRemoteKeys()

        val prevKey=if (pageKey == STARTING_PAGE_INDEX) null else pageKey-1
        val nextKey=if(pageKey >=response.total_pages) null else pageKey+1
        val keys=response.movieResponses.map {
            PopularMovieKeyEntity(it.id,prevKey,pageKey,nextKey)
        }
        insertAll(keys)
        insertAllMovies(response.toMovieEntity())
    }
}