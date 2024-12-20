package com.example.mymovielibrary.details.watched.presentation

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.mymovielibrary.movieList.data.remote.MovieApi
import com.example.mymovielibrary.movieList.util.RatingBar

@Composable
fun WatchedDetailsScreen() {
    val watchedDetailsViewModel = hiltViewModel<WatchedDetailsViewModel>()
    val watchedDetailsState = watchedDetailsViewModel.watchedDetailsState.collectAsState().value

    var showRatingDialog by remember { mutableStateOf(false) }

    val backdropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + watchedDetailsState.movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + watchedDetailsState.movie.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(modifier = Modifier.fillMaxSize()){
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
                        contentDescription = watchedDetailsState.movie.title
                    )
                }
            }

            if (backdropImageState is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    painter = backdropImageState.painter,
                    contentDescription = watchedDetailsState.movie.title,
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
                                contentDescription = watchedDetailsState.movie.title
                            )
                        }
                    }

                    if (posterImageState is AsyncImagePainter.State.Success) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            painter = posterImageState.painter,
                            contentDescription = watchedDetailsState.movie.title,
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(modifier = Modifier.padding(start = 16.dp), text = watchedDetailsState.movie.title, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row (
                        modifier = Modifier.padding(start = 16.dp)
                    ){
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = watchedDetailsState.movie.vote_average.div(2)
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = watchedDetailsState.movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(modifier = Modifier.padding(start = 16.dp), text = watchedDetailsState.movie.vote_count.toString() + " votes")

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(modifier = Modifier.padding(start = 16.dp), text = "Release Date: " + watchedDetailsState.movie.release_date)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (watchedDetailsState.movie.rating > 0f){
                Row (modifier = Modifier.padding(start = 16.dp)){
                    Text(
                        text = "Your Rating: ",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    RatingBar(
                        starsModifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                        rating = watchedDetailsState.movie.rating
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (watchedDetailsState.movie.review != ""){
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Review",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = watchedDetailsState.movie.review,
                    fontSize = 16.sp,
                )
            }
            else{
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Overview",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = watchedDetailsState.movie.overview,
                    fontSize = 16.sp,
                )
            }
        }

        Button(
            onClick = {
                showRatingDialog = true
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.BottomCenter)
        ) {
            Text(text = "Edit")
        }

        if (showRatingDialog) {
            var userRating by remember { mutableDoubleStateOf(watchedDetailsState.movie.rating) }
            var userReview by remember { mutableStateOf(watchedDetailsState.movie.review) }
            AlertDialog(
                onDismissRequest = { showRatingDialog = false },
                title = { Text("Edit") },
                text = {
                    Column {
                        Text("Rate:")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            RatingBar(
                                starsModifier = Modifier.size(24.dp),
                                rating = userRating,
                                onRatingChanged = { rating -> userRating = rating }
                            )

                            IconButton(
                                onClick = {
                                    userRating = 0.0
                                },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Delete Rating",
                                    tint = Color.Gray
                                )
                            }
                        }

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
                            watchedDetailsViewModel.updateReviewAndRating(watchedDetailsState.movie.id, userRating, userReview)
                            showRatingDialog = false
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showRatingDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}