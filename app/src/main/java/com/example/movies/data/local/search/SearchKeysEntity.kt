package com.example.movies.data.local.search

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "search_keys")
data class SearchKeysEntity(
    @PrimaryKey
    val movieId:Long,
    val preKey:Int?,
    val curKey:Int?,
    val nextKey:Int?

)
