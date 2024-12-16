package com.example.mymovielibrary.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymovielibrary.movieList.presentation.MovieListUIEvent
import com.example.mymovielibrary.movieList.presentation.MovieListViewModel
import com.example.mymovielibrary.movieList.presentation.PopularMoviesScreen
import com.example.mymovielibrary.movieList.presentation.UpcomingMoviesScreen
import com.example.mymovielibrary.movieList.presentation.WatchListScreen
import com.example.mymovielibrary.movieList.presentation.WatchedMoviesScreen
import com.example.mymovielibrary.movieList.util.Screen
import kotlin.reflect.KFunction2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                        if (movieListState.currentScreenIndex == 0) {
                            "Watched Movies"
                        }
                        else if (movieListState.currentScreenIndex == 1){
                            "Watch List"
                        }
                        else if (movieListState.currentScreenIndex == 2) {
                            "Popular Movies"
                        }
                        else {
                            "Upcoming Movies"
                        },
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)){
            NavHost(navController = bottomNavController, startDestination = Screen.Watched.rout) {
                composable(Screen.Watched.rout){
                    WatchedMoviesScreen(navController)
                }
                composable(Screen.WatchList.rout){
                    WatchListScreen(navController)
                }
                composable(Screen.PopularMovieList.rout){
                    PopularMoviesScreen(movieListState, navController, movieListViewModel::onEvent)
                }
                composable(Screen.UpcomingMovieList.rout){
                    UpcomingMoviesScreen(movieListState, navController, movieListViewModel::onEvent)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(bottomNavController: NavHostController, onEvent: (MovieListUIEvent) -> Unit){
    val items = listOf(
        BottomItem(
            title = "Watched",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "Watch List",
            icon = Icons.AutoMirrored.Rounded.List
        ),
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.MovieFilter
        ),
        BottomItem(
            title = "Upcoming",
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable{
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row (modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when(selected.intValue){
                            0 -> {
                                onEvent(MovieListUIEvent.Navigate(0))
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.Watched.rout)
                            }
                            1 -> {
                                onEvent(MovieListUIEvent.Navigate(1))
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.WatchList.rout)
                            }
                            2 -> {
                                onEvent(MovieListUIEvent.Navigate(2))
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.rout)
                            }
                            3 -> {
                                onEvent(MovieListUIEvent.Navigate(3))
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.rout)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)