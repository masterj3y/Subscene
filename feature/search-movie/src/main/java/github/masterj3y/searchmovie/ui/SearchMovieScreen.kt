package github.masterj3y.searchmovie.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.navigation.LocalNavController
import github.masterj3y.navigation.Route
import github.masterj3y.resources.R
import github.masterj3y.resources.composables.LoadingScreen
import github.masterj3y.searchmovie.SearchMovieViewModel

@Composable
fun SearchMovieScreen(viewModel: SearchMovieViewModel = hiltViewModel()) {

    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    val query by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = { SearchTopBar(query = query, onQueryChange = viewModel::search) }
    ) {

        Content(
            state = state,
            onNavToDetails = { path ->
                Route.MovieDetails.navigate(
                    navController = navController,
                    moviePath = path
                )
            }
        )
    }
}

@Composable
private fun SearchTopBar(query: String, onQueryChange: (String) -> Unit) {
    TopAppBar {
        Box(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
            SearchInput(
                placeholder = {
                    Text(text = stringResource(R.string.search_movie_input_placeholder))
                },
                value = query,
                onValueChange = onQueryChange
            )
        }
    }
}

@Composable
private fun Content(state: SearchMovieState, onNavToDetails: (String) -> Unit) {
    Crossfade(targetState = state, animationSpec = tween(700)) { newState ->

        when {
            newState.isLoading -> LoadingScreen()

            !newState.isLoading && !newState.hasAnErrorOccurred && newState.result.isNotEmpty() -> {
                if (newState.result.isNotEmpty())
                    Movies(
                        movies = newState.result,
                        onMovieClick = { movieItem ->
                            val moviePath = movieItem.url.substringAfterLast("/")
                            onNavToDetails(moviePath)
                        }
                    )
            }

            newState.hasAnErrorOccurred -> Error()
        }
    }
}

@Composable
private fun Error() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(text = stringResource(R.string.error_text))
    }
}