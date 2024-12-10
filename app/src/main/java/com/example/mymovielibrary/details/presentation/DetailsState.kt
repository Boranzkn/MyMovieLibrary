package com.example.mymovielibrary.details.presentation

import com.example.mymovielibrary.movieList.data.remote.respond.MovieDto

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: MovieDto? = null
)
