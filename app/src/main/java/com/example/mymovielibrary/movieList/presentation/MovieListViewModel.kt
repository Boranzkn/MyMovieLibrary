package com.example.mymovielibrary.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovielibrary.movieList.data.local.movie.MovieEntity
import com.example.mymovielibrary.movieList.data.local.movie.WatchedMovie
import com.example.mymovielibrary.movieList.domain.repository.MovieListRepository
import com.example.mymovielibrary.movieList.util.Category
import com.example.mymovielibrary.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState> = _movieListState

    init {
        getWatchedMovieList()
        getWatchList()
        getPopularMovieList()
        getUpcomingMovieList()
    }

    fun addToWatchList(newMovie: MovieEntity) {
        viewModelScope.launch {
            val updatedWatchList = _movieListState.value.watchList + newMovie
            _movieListState.value = _movieListState.value.copy(watchList = updatedWatchList)
            movieListRepository.setMovieToWatchList(newMovie)
        }
    }

    fun addToWatchedMovieList(newMovie: WatchedMovie) {
        viewModelScope.launch {
            val updatedWatchedMovieList = _movieListState.value.watchedMovieList + newMovie
            _movieListState.value = _movieListState.value.copy(watchedMovieList = updatedWatchedMovieList)
            movieListRepository.setMovieToWatched(newMovie)
        }
    }

    fun deleteMovieFromWatchListById(id: Int) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getWatchListMovie(id).collect { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        if (result.data != null){
                            val updatedWatchList = _movieListState.value.watchList - result.data
                            _movieListState.value = _movieListState.value.copy(watchList = updatedWatchList)
                            movieListRepository.deleteMovieFromWatchListById(id)
                        }
                    }
                }
            }
        }
    }

    fun deleteMovieFromWatchedMovieListById(id: Int) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getWatchedMovie(id).collect { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        if (result.data != null){
                            val updatedWatchedMovieList = _movieListState.value.watchedMovieList - result.data
                            _movieListState.value = _movieListState.value.copy(watchedMovieList = updatedWatchedMovieList)
                            movieListRepository.deleteMovieFromWatchedMovieListById(id)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: MovieListUIEvent){
        when(event){
            is MovieListUIEvent.Paginate -> {
                if (event.category == Category.POPULAR){
                    getPopularMovieList()
                }
                else if (event.category == Category.UPCOMING){
                    getUpcomingMovieList()
                }
            }

            is MovieListUIEvent.Navigation -> {
                when(event.index){
                    0 -> {
                        _movieListState.update {
                            it.copy(currentScreenIndex = 0)
                        }
                    }
                    1 -> {
                        _movieListState.update {
                            it.copy(currentScreenIndex = 1)
                        }
                    }
                    2 -> {
                        _movieListState.update {
                            it.copy(currentScreenIndex = 2)
                        }
                    }
                    3 -> {
                        _movieListState.update {
                            it.copy(currentScreenIndex = 3)
                        }
                    }
                    else -> {
                        _movieListState.update {
                            it.copy(currentScreenIndex = 4)
                        }
                    }
                }
            }
        }
    }

    private fun getPopularMovieList(){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieListFromApi(
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList,
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieListFromApi(
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList
                                            + upcomingList,
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getWatchedMovieList(){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getWatchedMovieList().collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { watchedList ->
                            _movieListState.update {
                                it.copy(
                                    watchedMovieList = watchedList
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getWatchList() {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getWatchList().collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { watchList ->
                            _movieListState.update {
                                it.copy(
                                    watchList = watchList
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun isInWatchedMovieList(id: Int): Boolean {
        return _movieListState.value.watchedMovieList.any { it.id == id }
    }
}