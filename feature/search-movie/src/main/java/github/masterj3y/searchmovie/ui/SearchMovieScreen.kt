package github.masterj3y.searchmovie.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.navigation.LocalNavController
import github.masterj3y.navigation.Route
import github.masterj3y.resources.R
import github.masterj3y.searchmovie.SearchMovieViewModel
import github.masterj3y.searchmovie.model.MovieItem
import kotlinx.coroutines.delay

@Composable
fun SearchMovieScreen(viewModel: SearchMovieViewModel = hiltViewModel()) {

    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    val (movieTitle, setMovieTitle) = rememberSaveable { mutableStateOf("") }

    val onEvent = remember {
        { event: SearchMovieEvent -> viewModel.onEvent(event) }
    }

    LaunchedEffect(movieTitle) {
        delay(700)
        if (movieTitle.isNotBlank())
            onEvent(SearchMovieEvent.Search(movieTitle))
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    SearchInput(
                        placeholder = {
                            Text(text = stringResource(R.string.search_movie_input_placeholder))
                        },
                        value = movieTitle,
                        onValueChange = setMovieTitle
                    )
                }
            }
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            when (state) {

                is SearchMovieState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is SearchMovieState.Result -> {
                    val resultState = state as? SearchMovieState.Result
                    Movies(
                        movies = resultState?.movies ?: remember { mutableStateListOf() },
                        onMovieClick = { movieItem ->
                            val moviePath = movieItem.url.substringAfterLast("/")
                            Route.MovieDetails.navigate(
                                navController = navController,
                                moviePath = moviePath
                            )
                        }
                    )
                }

                is SearchMovieState.Error -> Error()
            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Movies(
    movies: SnapshotStateList<MovieItem>,
    onMovieClick: (MovieItem) -> Unit
) {

    LazyColumn {
        items(items = movies) {
            MovieItem(
                movie = it,
                onClick = onMovieClick
            )
        }
    }
}

@Composable
private fun MovieItem(movie: MovieItem, onClick: (MovieItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colors.primary.copy(alpha = .1f))
            .clickable { onClick(movie) }
            .padding(16.dp)
    ) {

        Text(text = movie.title)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.movie_subtitle_count, movie.subtitles),
            style = MaterialTheme.typography.body2
        )
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