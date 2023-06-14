package com.example.movies.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Serializable is a markable interface or we can call as an empty interface. It doesn’t have any pre-implemented methods. Serializable is going to convert an object to byte stream. So the user can pass the data between one activity to another activity. The main advantage of serializable is the creation and passing data is very easy but it is a slow process compare to parcelable.

//Parcel able is faster than serializable. Parcel able is going to convert object to byte stream and pass the data between two activities. Writing parcel able code is little bit complex compare to serialization. It doesn’t create more temp objects while passing the data between two activities.
@Parcelize
data class Movie(
    val id:Long,
    val title:String,
    val overview: String,

    val rate100: Int,
    val releaseDate: String?,
    val posterUrl: String?,
    val popularity: Float,
): Parcelable