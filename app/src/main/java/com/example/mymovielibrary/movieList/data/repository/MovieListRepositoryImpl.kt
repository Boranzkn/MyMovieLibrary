package com.example.mymovielibrary.movieList.data.repository

import com.example.mymovielibrary.movieList.data.local.movie.MovieDatabase
import com.example.mymovielibrary.movieList.data.mappers.toMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovieEntity
import com.example.mymovielibrary.movieList.data.remote.MovieApi
import com.example.mymovielibrary.movieList.data.remote.respond.MovieDto
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.domain.repository.MovieListRepository
import com.example.mymovielibrary.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
): MovieListRepository {
    override suspend fun getMovieListFromDatabase(category: String): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            if (localMovieList.isNotEmpty()) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }
            else{
                // Write Watched or WatchList is empty to the screen
                TODO("Not yet implemented")
            }
        }
    }

    override suspend fun getMovieListFromApi(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val movieList = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieList.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieFromDatabase(id: Int): Flow<Resource<Movie>> {
        return flow {

            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity.toMovie(movieEntity.category))
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieFromApi(id: Int): Flow<Resource<MovieDto>> {
        return flow {

            emit(Resource.Loading(true))

            val movieDto = try {
                movieApi.getMovieById(id)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            if (movieDto != null) {
                emit(
                    Resource.Success(movieDto)
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))
        }
    }

}