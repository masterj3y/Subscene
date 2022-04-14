package github.masterj3y.searchmovie.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.navigation.LocalNavController
import github.masterj3y.navigation.Route
import github.masterj3y.resources.R
import github.masterj3y.resources.composables.SimpleTab
import github.masterj3y.searchmovie.SearchMovieViewModel
import github.masterj3y.searchmovie.model.MovieItem
import kotlinx.coroutines.delay

@Composable
fun SearchMovieScreen(viewModel: SearchMovieViewModel = hiltViewModel()) {

    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    val (movieTitle, setMovieTitle) = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(movieTitle) {
        delay(700)
        if (movieTitle.isNotBlank())
            viewModel.search(movieTitle)
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Box(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
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

            when {

                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                !state.isLoading && !state.hasAnErrorOccurred && state.result.isNotEmpty() -> {
                    if (state.result.isNotEmpty())
                        Movies(
                            movies = state.result,
                            onMovieClick = { movieItem ->
                                val moviePath = movieItem.url.substringAfterLast("/")
                                Route.MovieDetails.navigate(
                                    navController = navController,
                                    moviePath = moviePath
                                )
                            }
                        )
                }

                state.hasAnErrorOccurred -> Error()
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Movies(
    movies: Map<String, List<MovieItem>>,
    onMovieClick: (MovieItem) -> Unit
) {

    val (categoryKey, setCategoryKey) = remember {
        mutableStateOf(movies.keys.toList().first())
    }

    val items = remember(categoryKey) {
        mutableStateOf(movies[categoryKey])
    }

    LazyColumn {
        item {
            Tabs(
                tabs = movies.keys,
                selectedTab = categoryKey,
                onClick = setCategoryKey
            )
        }
        items(items = items.value ?: listOf()) {
            MovieItem(
                movie = it,
                onClick = onMovieClick
            )
        }
    }
}

@Composable
private fun Tabs(
    tabs: Set<String>,
    selectedTab: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        LazyRow {

            item {
                Spacer(modifier = Modifier.width(8.dp))
            }

            items(tabs.toList()) {
                SimpleTab(
                    text = it,
                    isSelected = it == selectedTab,
                    selectedColor = MaterialTheme.colors.background,
                    unselectedColor = MaterialTheme.colors.primary,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun MovieItem(movie: MovieItem, onClick: (MovieItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
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