package com.example.movies.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.repository.movies.MovieLocalDataSource
import kotlinx.coroutines.flow.Flow
@Dao
interface MovieDao:MovieLocalDataSource{



    @Query("select * from movies where id =:id")
    override fun getMovieFlow(id: Long): Flow<MovieEntity?>

@Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(movieEntity: MovieEntity)
}