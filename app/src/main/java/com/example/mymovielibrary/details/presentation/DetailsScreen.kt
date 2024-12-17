package com.example.mymovielibrary.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.mymovielibrary.details.watched.presentation.WatchedDetailsViewModel
import com.example.mymovielibrary.movieList.data.mappers.toMovieEntity
import com.example.mymovielibrary.movieList.data.mappers.toWatchedMovie
import com.example.mymovielibrary.movieList.data.remote.MovieApi
import com.example.mymovielibrary.movieList.presentation.MovieListViewModel
import com.example.mymovielibrary.movieList.util.Category
import com.example.mymovielibrary.movieList.util.RatingBar

@Composable
fun DetailsScreen() {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value

    var showRatingDialog by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(0f) }
    var userReview by remember { mutableStateOf("") }
    var deleteFromWatchlist by remember { mutableStateOf(false) }

    val isInWatchList = remember(detailsState.movie, movieListState.watchList) {
        derivedStateOf {
            detailsState.movie != null && movieListState.watchList.any { it.id == detailsState.movie.id }
        }
    }

    val isInWatchedMovieList = remember(detailsState.movie, movieListState.watchedMovieList) {
        derivedStateOf {
            detailsState.movie != null && movieListState.watchedMovieList.any { it.id == detailsState.movie.id }
        }
    }

    val backdropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (backdropImageState is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = detailsState.movie?.title
                    )
                }
            }

            if (backdropImageState is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    painter = backdropImageState.painter,
                    contentDescription = detailsState.movie?.title,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(240.dp)
                ) {
                    if (posterImageState is AsyncImagePainter.State.Error) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(70.dp),
                                imageVector = Icons.Rounded.ImageNotSupported,
                                contentDescription = detailsState.movie?.title
                            )
                        }
                    }

                    if (posterImageState is AsyncImagePainter.State.Success) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            painter = posterImageState.painter,
                            contentDescription = detailsState.movie?.title,
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                detailsState.movie?.let { movie ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(modifier = Modifier.padding(start = 16.dp), text = movie.title, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.padding(start = 16.dp)) {
                            RatingBar(
                                starsModifier = Modifier.size(18.dp),
                                rating = movie.vote_average.div(2)
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = movie.vote_average.toString().take(3),
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(modifier = Modifier.padding(start = 16.dp), text = movie.vote_count.toString() + " votes")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(modifier = Modifier.padding(start = 16.dp), text = "Release Date: " + movie.release_date)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(modifier = Modifier.padding(start = 16.dp), text = "Overview", fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            detailsState.movie?.let {
                Text(modifier = Modifier.padding(start = 16.dp), text = it.overview, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            detailsState.movie?.let { movie ->
                when {
                    isInWatchedMovieList.value -> {
                        // No buttons if already watched
                    }
                    isInWatchList.value -> {
                        Button(
                            onClick = {
                                deleteFromWatchlist = true
                                showRatingDialog = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Watched")
                        }
                    }
                    else -> {
                        Button(
                            onClick = {
                                detailsViewModel.addToWatchList(movie.toMovieEntity(Category.WATCHLIST))
                                movieListViewModel.addToWatchList(movie.toMovieEntity(Category.WATCHLIST))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Add to Watch List")
                        }
                        Button(
                            onClick = {
                                showRatingDialog = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Watched")
                        }
                    }
                }
            }
        }

        if (showRatingDialog) {
            AlertDialog(
                onDismissRequest = { showRatingDialog = false },
                title = { Text("Rate and Review") },
                text = {
                    Column {
                        Text("Rate:")
                        Spacer(modifier = Modifier.height(8.dp))
                        RatingBar(
                            starsModifier = Modifier.size(24.dp),
                            rating = userRating.toDouble(),
                            onRatingChanged = { rating -> userRating = rating.toFloat() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Review:")
                        BasicTextField(
                            value = userReview,
                            onValueChange = { userReview = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp),
                            singleLine = false
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (deleteFromWatchlist){
                                detailsViewModel.removeFromWatchList(detailsState.movie!!.id)
                                movieListViewModel.removeFromWatchList(detailsState.movie.id)
                                deleteFromWatchlist = false
                            }

                            detailsViewModel.addToWatchedMovieList(detailsState.movie!!.toWatchedMovie(userReview, userRating))
                            movieListViewModel.addToWatchedMovieList(detailsState.movie.toWatchedMovie(userReview, userRating))
                            showRatingDialog = false
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        deleteFromWatchlist = false
                        showRatingDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}