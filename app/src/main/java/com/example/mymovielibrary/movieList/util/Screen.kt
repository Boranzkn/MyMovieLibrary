package com.example.mymovielibrary.movieList.util

sealed class Screen(val rout: String) {
    data object Home : Screen("main")
    data object Watched : Screen("watched")
    data object WatchList : Screen("watchList")
    data object PopularMovieList : Screen("popularMovie")
    data object UpcomingMovieList : Screen("upcomingMovie")
    data object Details : Screen("details")
    data object WatchedDetails : Screen("watchedDetails")
    data object Search : Screen("search")
}