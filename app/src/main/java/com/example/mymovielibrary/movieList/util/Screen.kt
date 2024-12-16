package com.example.mymovielibrary.movieList.util

sealed class Screen(val rout: String) {
    object Home : Screen("main")
    object Watched : Screen("watched")
    object WatchList : Screen("watchList")
    object PopularMovieList : Screen("popularMovie")
    object UpcomingMovieList : Screen("upcomingMovie")
    object Details : Screen("details")
    object WatchedDetails : Screen("watchedDetails")
    object Search : Screen("search")
}