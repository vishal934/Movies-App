package com.example.movies.data.local.search

import androidx.paging.PagingSource
import androidx.room.*
import com.example.movies.data.local.CommonMovieListDao
import com.example.movies.data.local.movie.MovieEntity
import com.example.movies.data.remote.STARTING_PAGE_INDEX
import com.example.movies.data.remote.models.MoviePageListResponse
import com.example.movies.data.repository.search.SearchLocalDataSource
import javax.sql.CommonDataSource

@Dao
interface  SearchDao:SearchLocalDataSource,CommonMovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searchKey: List<SearchKeysEntity>)

    @Query("SELECT * FROM search_keys WHERE movieId = :movieId")
    override suspend fun getSearchKeysForMoviesID(movieId: Long): SearchKeysEntity?

    @Query("DELETE FROM search_keys")
    suspend fun clearRemoteKeys()

    @Query(
        """SELECT * FROM movies
            INNER JOIN search_keys  ON movies.id=search_keys.movieId
            WHERE title LIKE :queryString OR overview LIKE :queryString 
            ORDER BY curKey ASC, popularity DESC, title ASC"""
    )
    @RewriteQueriesToDropUnusedColumns
    override fun searchMovies(queryString: String): PagingSource<Int, MovieEntity>

    @Transaction
    override suspend fun cacheResponse(
        response: MoviePageListResponse,
        pageKey: Int,
        isRefresh: Boolean,
    ) {
        if (isRefresh) {
            clearRemoteKeys()
        }
        val prevKey = if (pageKey == STARTING_PAGE_INDEX) null else pageKey - 1
        val nextKey = if (pageKey >= response.total_pages) null else pageKey + 1
        val keys = response.movieResponses.map { SearchKeysEntity(it.id, prevKey, pageKey, nextKey) }
        insertAll(keys)
        insertAllMovies(response.toMovieEntity())
    }

}