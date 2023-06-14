package com.example.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.movies.data.local.movie.MovieEntity


interface CommonMovieListDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movie:List<MovieEntity?>)
}