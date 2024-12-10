package com.example.mymovielibrary.movieList.domain.repository

import com.example.mymovielibrary.movieList.data.remote.respond.MovieDto
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieListFromDatabase(
        category: String
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieListFromApi(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieFromDatabase(id: Int): Flow<Resource<Movie>>

    suspend fun getMovieFromApi(id: Int): Flow<Resource<MovieDto>>
}
