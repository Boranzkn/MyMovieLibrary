package com.example.mymovielibrary.movieList.presentation

import com.example.mymovielibrary.movieList.data.local.movie.MovieEntity
import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie
import com.example.mymovielibrary.movieList.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val currentScreenIndex: Int = 0,

    val watchedMovieList: List<WatchedMovie> = emptyList(),
    val watchList: List<MovieEntity> = emptyList(),
    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)