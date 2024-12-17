package com.example.mymovielibrary.search.data.repository

import com.example.mymovielibrary.search.data.remote.api.SearchApi
import com.example.mymovielibrary.search.domain.repository.SearchRepository
import com.example.mymovielibrary.movieList.data.mappers.toMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovieEntity
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.util.Category
import com.example.mymovielibrary.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun getSearchList(
        query: String, page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val searchList = try {
                searchApi.getSearchList(
                    query, page
                )?.results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(
                    Resource.Error("Could not load data")
                )
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(
                    Resource.Error("Could not load data")
                )
                emit(Resource.Loading(false))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Resource.Error("Could not load data")
                )
                emit(Resource.Loading(false))
                return@flow
            }

            searchList?.let { movieDtos ->
                val movieList = movieDtos.map { movieDto ->
                    movieDto.toMovieEntity(Category.SEARCH).toMovie(Category.SEARCH)
                }

                emit(Resource.Success(movieList))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(
                Resource.Error("Could not load data")
            )
            emit(Resource.Loading(false))
        }
    }
}