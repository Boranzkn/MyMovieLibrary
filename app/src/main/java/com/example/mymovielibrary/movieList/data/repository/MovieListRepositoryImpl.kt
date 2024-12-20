package com.example.mymovielibrary.movieList.data.repository

import com.example.mymovielibrary.movieList.data.local.movie.MovieDatabase
import com.example.mymovielibrary.movieList.data.local.movie.MovieEntity
import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovieEntity
import com.example.mymovielibrary.movieList.data.remote.MovieApi
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.domain.repository.MovieListRepository
import com.example.mymovielibrary.movieList.util.Category
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

    override suspend fun getWatchList(): Flow<Resource<List<MovieEntity>>> {
        return flow {

            emit(Resource.Loading(true))

            val watchList = movieDatabase.movieDao.getWatchListMovies()

            if (watchList.isNotEmpty()) {
                emit(Resource.Success( data = watchList ))

                emit(Resource.Loading(false))
                return@flow
            }
        }
    }

    override suspend fun getWatchedMovieList(): Flow<Resource<List<WatchedMovie>>> {
        return flow {

            emit(Resource.Loading(true))

            val watchedMovieList = movieDatabase.movieDao.getWatchedMovieList()

            if (watchedMovieList.isNotEmpty()) {
                emit(Resource.Success( data = watchedMovieList ))

                emit(Resource.Loading(false))
                return@flow
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

    override suspend fun getWatchedMovie(id: Int): Flow<Resource<WatchedMovie>> {
        return flow {

            emit(Resource.Loading(true))

            val watchedMovie = movieDatabase.movieDao.getWatchedMovieById(id)

            if (watchedMovie != null) {
                emit(
                    Resource.Success(watchedMovie)
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getWatchListMovie(id: Int): Flow<Resource<MovieEntity>> {
        return flow {

            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getWatchListMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity)
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieFromApi(id: Int): Flow<Resource<Movie>> {
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
                    Resource.Success(movieDto.toMovieEntity(Category.POPULAR).toMovie(Category.POPULAR))
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Error no such movie"))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun setMovieToWatchList(movie: MovieEntity) {
        movieDatabase.movieDao.upsertMovieToWatchList(movie)
    }

    override suspend fun setMovieToWatched(movie: WatchedMovie) {
        movieDatabase.movieDao.upsertMovieToWatched(movie)
    }

    override suspend fun deleteMovieFromWatchListById(id: Int) {
        movieDatabase.movieDao.deleteMovieFromWatchListById(id)
    }

    override suspend fun deleteMovieFromWatchedMovieListById(id: Int) {
        movieDatabase.movieDao.deleteMovieFromWatchedMovieListById(id)
    }
}