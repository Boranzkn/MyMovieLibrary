package com.example.mymovielibrary.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mymovielibrary.movieList.data.mappers.toMovie
import com.example.mymovielibrary.movieList.presentation.component.MovieItem
import com.example.mymovielibrary.movieList.util.Category

@Composable
fun WatchListScreen(
    navController: NavHostController
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value

    if (movieListState.watchList.isEmpty()){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
    else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.watchList.size){index ->
                MovieItem(
                    movie = movieListState.watchList[index].toMovie(Category.WATCHLIST),
                    navHostController =  navController,
                    onDeleteClick = {
                        movieListViewModel.deleteMovieFromWatchListById(movieListState.watchList[index].id)
                    }
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}