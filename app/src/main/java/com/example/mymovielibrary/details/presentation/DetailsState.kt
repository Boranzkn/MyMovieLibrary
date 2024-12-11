package com.example.mymovielibrary.details.presentation

import com.example.mymovielibrary.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
