package com.example.mymovielibrary.movieList.presentation

sealed interface MovieListUIEvent {
    data class Paginate(val category: String): MovieListUIEvent
    data class Navigation(val index: Int): MovieListUIEvent
}