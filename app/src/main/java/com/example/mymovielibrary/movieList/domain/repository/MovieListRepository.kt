package com.example.mymovielibrary.movieList.domain.repository

import com.example.mymovielibrary.movieList.data.local.movie.MovieEntity
import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getWatchList(): Flow<Resource<List<MovieEntity>>>

    suspend fun getWatchedMovieList(): Flow<Resource<List<WatchedMovie>>>

    suspend fun getMovieListFromApi(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getWatchedMovie(id: Int): Flow<Resource<WatchedMovie>>

    suspend fun getWatchListMovie(id: Int): Flow<Resource<MovieEntity>>

    suspend fun getMovieFromApi(id: Int): Flow<Resource<Movie>>

    suspend fun setMovieToWatchList(movie: MovieEntity)

    suspend fun setMovieToWatched(movie: WatchedMovie)

    suspend fun deleteMovieFromWatchListById(id: Int)
}
