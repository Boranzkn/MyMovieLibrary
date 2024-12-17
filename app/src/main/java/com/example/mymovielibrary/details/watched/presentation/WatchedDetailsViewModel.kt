package com.example.mymovielibrary.details.watched.presentation

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
class WatchedDetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")
    private var _watchedDetailsState = MutableStateFlow(WatchedDetailsState())
    val watchedDetailsState = _watchedDetailsState.asStateFlow()

    init {
        getMovie(movieId ?: -1)
    }

    private fun getMovie(id: Int){
        viewModelScope.launch {
            _watchedDetailsState.update { it.copy(isLoading = true) }

            movieListRepository.getWatchedMovie(id).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _watchedDetailsState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _watchedDetailsState.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _watchedDetailsState.update { it.copy(movie = movie) }
                        }
                    }
                }
            }
        }
    }

    fun addToWatched(movie: WatchedMovie?){
        viewModelScope.launch{
            movie?.let {
                movieListRepository.setMovieToWatched(movie)
            }
        }
    }
}