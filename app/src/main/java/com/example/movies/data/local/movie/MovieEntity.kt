package com.example.movies.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies.data.remote.CDN_BASE_URL
import com.example.movies.domain.Movie
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieEntity(

    @PrimaryKey
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("vote_average") val rate: Float,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("popularity") val popularity: Float,
        )

{  fun toMovie()=Movie(
    id=id,
    title=title,
    overview=overview,

    rate100 = (rate *10).toInt(),
    releaseDate=releaseDate,
    posterUrl = CDN_BASE_URL+posterPath,
    popularity=popularity )
}