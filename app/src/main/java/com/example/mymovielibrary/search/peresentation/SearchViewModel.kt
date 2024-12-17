package com.example.mymovielibrary.search.peresentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovielibrary.search.domain.repository.SearchRepository
import com.example.mymovielibrary.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _navigateToDetailsChannel = Channel<Int>()
    val navigateToDetailsChannel = _navigateToDetailsChannel.receiveAsFlow()

    private var searchJob: Job? = null

    fun onEvent(searchUiEvent: SearchUiEvents) {
        when (searchUiEvent) {
            is SearchUiEvents.OnSearchItemClick -> {
                viewModelScope.launch {
                    _navigateToDetailsChannel.send(
                        searchUiEvent.movie.id
                    )
                }
            }

            is SearchUiEvents.OnSearchQueryChange -> {

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    _searchState.update {
                        it.copy(
                            searchPage = 1,
                            searchQuery = searchUiEvent.searchQuery,
                            searchList = emptyList()
                        )
                    }
                    if (searchState.value.searchQuery.isNotEmpty()) {
                        loadSearchList()
                    }
                }
            }

            SearchUiEvents.OnPaginate -> {
                _searchState.update {
                    it.copy(
                        searchPage = it.searchPage + 1
                    )
                }
                loadSearchList()
            }
        }
    }

    private fun loadSearchList() {
        viewModelScope.launch {
            searchRepository.getSearchList(
                query = searchState.value.searchQuery,
                page = searchState.value.searchPage
            ).collect { result ->
                when (result) {
                    is Resource.Error -> Unit

                    is Resource.Loading -> {
                        _searchState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movieList ->
                            _searchState.update {
                                it.copy(
                                    searchList = it.searchList + movieList
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}






















