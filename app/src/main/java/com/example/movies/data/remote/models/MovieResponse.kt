package com.example.movies.data.remote.models

import com.example.movies.data.local.movie.MovieEntity
import com.google.gson.annotations.SerializedName

data class MovieResponse(

    val genre_ids: List<Int>,
    @SerializedName("id")  val id: Long,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity")   val popularity: Float,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date")  val release_date: String,
    @SerializedName("title")   val title: String,
    @SerializedName("vote_average") val rate: Float,

    val vote_count: Int
)
{
    fun toMovieEntity()=MovieEntity(
        id=id,
        title=title,
        overview=overview,
        rate = rate ,
        releaseDate = release_date,
        posterPath = poster_path,
        popularity = popularity,

    )
}