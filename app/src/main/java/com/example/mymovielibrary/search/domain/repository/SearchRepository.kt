package com.example.mymovielibrary.search.domain.repository

import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getSearchList(
        query: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

}















