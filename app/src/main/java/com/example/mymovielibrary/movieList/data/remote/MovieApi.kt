package com.example.mymovielibrary.movieList.data.remote

import com.example.mymovielibrary.BuildConfig
import com.example.mymovielibrary.movieList.data.remote.respond.MovieDto
import com.example.mymovielibrary.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") api_key: String = API_KEY
    ): MovieListDto

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): MovieDto

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.TMDB_API_KEY
    }
}