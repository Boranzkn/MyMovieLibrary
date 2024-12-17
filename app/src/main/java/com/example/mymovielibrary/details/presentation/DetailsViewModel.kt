package com.example.mymovielibrary.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovielibrary.movieList.data.local.movie.MovieEntity
import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovie
import com.example.mymovielibrary.movieList.data.mappers.toMovieEntity
import com.example.mymovielibrary.movieList.data.remote.respond.MovieDto
import com.example.mymovielibrary.movieList.domain.repository.MovieListRepository
import com.example.mymovielibrary.movieList.presentation.MovieListState
import com.example.mymovielibrary.movieList.util.Category
import com.example.mymovielibrary.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")
    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        getMovie(movieId ?: -1)
    }

    private fun getMovie(id: Int){
        viewModelScope.launch {
            _detailsState.update { it.copy(isLoading = true) }

            movieListRepository.getMovieFromApi(id).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        getMovieFromDB(id)
                    }
                    is Resource.Loading -> {
                        _detailsState.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailsState.update { it.copy(movie = movie) }
                        }
                    }
                }
            }
        }
    }

    private fun getMovieFromDB(id: Int){
        viewModelScope.launch {
            movieListRepository.getWatchListMovie(id).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _detailsState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _detailsState.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailsState.update { it.copy(movie = movie.toMovie(Category.WATCHLIST)) }
                        }
                    }
                }
            }
        }
    }

    fun addToWatchList(movie: MovieEntity){
        viewModelScope.launch{
            movieListRepository.setMovieToWatchList(movie)
        }
    }

    fun addToWatchedMovieList(movie: WatchedMovie){
        viewModelScope.launch{
            movieListRepository.setMovieToWatched(movie)
        }
    }

    fun removeFromWatchList(id: Int) {
        viewModelScope.launch{
            movieListRepository.deleteMovieFromWatchListById(id)
        }
    }
}