package com.example.mymovielibrary.search.peresentation

import com.example.mymovielibrary.movieList.domain.model.Movie

data class SearchState (
    val isLoading: Boolean = false,
    val searchPage: Int = 1,
    val searchQuery: String = "",
    val searchList: List<Movie> = emptyList()
)





















