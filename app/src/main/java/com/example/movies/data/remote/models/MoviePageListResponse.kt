package com.example.movies.data.remote.models

import com.example.movies.data.local.movie.MovieEntity
import com.google.gson.annotations.SerializedName

data class MoviePageListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movieResponses: List<MovieResponse>,
    @SerializedName("total_pages") val total_pages: Int,

    ){
    fun toMovieEntity()=movieResponses.map {
        MovieEntity(
            id=it.id,
            title = it.title,
            overview = it.overview,
            rate = it.rate,
            releaseDate = it.release_date,
            posterPath = it.poster_path,
            popularity = it.popularity
        )
    }
}