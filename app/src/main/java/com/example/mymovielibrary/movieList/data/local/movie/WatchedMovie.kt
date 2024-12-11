package com.example.mymovielibrary.movieList.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymovielibrary.movieList.util.Category

@Entity
data class WatchedMovie(
    val backdrop_path: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val category: String = Category.WATCHED,

    @PrimaryKey
    val id: Int
)