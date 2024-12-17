package com.example.mymovielibrary.movieList.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymovielibrary.movieList.util.Category

@Entity
data class WatchedMovie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val category: String = Category.WATCHED,
    val rating: Float,
    val review: String,

    @PrimaryKey
    val id: Int
)