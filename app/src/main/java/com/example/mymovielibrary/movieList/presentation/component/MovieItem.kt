package com.example.mymovielibrary.movieList.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.mymovielibrary.movieList.data.remote.MovieApi
import com.example.mymovielibrary.movieList.domain.model.Movie
import com.example.mymovielibrary.movieList.presentation.MovieListViewModel
import com.example.mymovielibrary.movieList.util.Category
import com.example.mymovielibrary.movieList.util.RatingBar
import com.example.mymovielibrary.movieList.util.Screen
import com.example.mymovielibrary.movieList.util.getAverageColor

@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController,
    onDeleteClick: () -> Unit
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movie.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember { mutableStateOf(defaultColor) }

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        dominantColor
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    if (movieListViewModel.isInWatchedMovieList(movie.id)){
                        navHostController.navigate(Screen.WatchedDetails.rout + "/${movie.id}")
                    }
                    else{
                        navHostController.navigate(Screen.Details.rout + "/${movie.id}")
                    }
                }
        ) {
            if (imageState is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = movie.title
                    )
                }
            }

            if (imageState is AsyncImagePainter.State.Success) {
                dominantColor =
                    getAverageColor(imageState.result.drawable.toBitmap().asImageBitmap())

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(22.dp)),
                    painter = imageState.painter,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    text = movie.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 12.dp, top = 4.dp)
                ) {
                    RatingBar(
                        starsModifier = Modifier.size(18.dp),
                        rating = movie.vote_average / 2
                    )

                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = movie.vote_average.toString().take(3),
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }
        }

        if (movie.category != Category.POPULAR && movie.category != Category.UPCOMING){
            IconButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Movie",
                    tint = Color.Red
                )
            }
        }
    }
}
