package github.masterj3y.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

@Composable
fun Navigation(navController: NavHostController, builder: NavGraphBuilder.() -> Unit) {

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Route.SearchMovie.route,
            builder = builder
        )
    }
}

sealed class Route(val route: String) {

    object SearchMovie : Route("search-movie")
}