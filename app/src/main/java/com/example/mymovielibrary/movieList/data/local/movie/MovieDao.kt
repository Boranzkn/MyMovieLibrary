package com.example.mymovielibrary.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieToWatchList(movie: MovieEntity)

    @Upsert
    suspend fun upsertMovieToWatched(movie: WatchedMovie)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getWatchListMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM WatchedMovie WHERE id = :id")
    suspend fun getWatchedMovieById(id: Int): WatchedMovie

    @Query("SELECT * FROM MovieEntity")
    suspend fun getWatchListMovies(): List<MovieEntity>

    @Query("SELECT * FROM WatchedMovie")
    suspend fun getWatchedMovieList(): List<WatchedMovie>
}