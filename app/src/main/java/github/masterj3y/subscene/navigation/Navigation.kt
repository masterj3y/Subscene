package github.masterj3y.subscene.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import github.masterj3y.navigation.Navigation
import github.masterj3y.navigation.Route
import github.masterj3y.searchmovie.screen.SearchMovieScreen

@Composable
internal fun MainNavigation(
    navController: NavHostController
) {

    Navigation(navController) {

        composable(Route.SearchMovie.route) { SearchMovieScreen() }
    }
}