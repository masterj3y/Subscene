package github.masterj3y.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun Navigation(navController: NavHostController, builder: NavGraphBuilder.() -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Route.SearchMovie.route,
        builder = builder
    )
}

sealed class Route(val route: String) {

    object SearchMovie : Route("search-movie")

    object MovieDetails : Route("subtitles") {
        const val ARG_MOVIE_PATH = "movie-path"
    }
}