package com.example.mymovielibrary.details.watched.presentation

import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie

data class WatchedDetailsState(
    val isLoading: Boolean = false,
    val movie: WatchedMovie? = null
)
