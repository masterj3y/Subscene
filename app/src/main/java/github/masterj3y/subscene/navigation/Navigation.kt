package github.masterj3y.subscene.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import github.masterj3y.navigation.Navigation
import github.masterj3y.navigation.Route
import github.masterj3y.searchmovie.ui.SearchMovieScreen
import github.masterj3y.subtitle.ui.SubtitlesScreen

@Composable
internal fun MainNavigation(
    navController: NavHostController
) {

    Navigation(navController) {

        composable(Route.SearchMovie.route) { SearchMovieScreen() }

        composable(
            route = "${Route.Subtitles.route}/{${Route.Subtitles.ARG_MOVIE_PATH}}",
            arguments = listOf(
                navArgument(name = Route.Subtitles.ARG_MOVIE_PATH) {
                    type = NavType.StringType
                }
            )
        ) {
            SubtitlesScreen(
                moviePath =
                navController
                    .currentBackStackEntry
                    ?.arguments
                    ?.getString(Route.Subtitles.ARG_MOVIE_PATH)
            )
        }
    }
}