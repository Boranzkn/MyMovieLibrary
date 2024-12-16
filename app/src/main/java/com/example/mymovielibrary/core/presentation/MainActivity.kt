package com.example.mymovielibrary.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymovielibrary.details.presentation.DetailsScreen
import com.example.mymovielibrary.details.watched.presentation.WatchedDetailsScreen
import com.example.mymovielibrary.movieList.util.Screen
import com.example.mymovielibrary.ui.theme.MyMovieLibraryTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMovieLibraryTheme {
                SetBarColor(MaterialTheme.colorScheme.inverseOnSurface)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.rout
                    ) {
                        composable(Screen.Home.rout){
                            HomeScreen(navController)
                        }

                        composable(Screen.Details.rout + "/{movieId}", arguments = listOf(
                            navArgument("movieId") {type = NavType.IntType}
                        )
                        ){
                            DetailsScreen()
                        }

                        composable(Screen.WatchedDetails.rout + "/{movieId}", arguments = listOf(
                            navArgument("movieId") {type = NavType.IntType}
                        )
                        ){
                            WatchedDetailsScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color){
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}