package com.example.mymovielibrary.search.peresentation

import com.example.mymovielibrary.movieList.domain.model.Movie

sealed class SearchUiEvents {
    data class OnSearchQueryChange (
        val searchQuery: String
    ): SearchUiEvents()

    data class OnSearchItemClick(
        val movie: Movie
    ): SearchUiEvents()

    data object OnPaginate: SearchUiEvents()
}






















