package github.masterj3y.searchmovie.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.designsystem.R
import github.masterj3y.designsystem.components.Loading
import github.masterj3y.designsystem.components.SimpleTab
import github.masterj3y.extensions.compose.rememberFlowWithLifecycle
import github.masterj3y.searchmovie.SearchMovieViewModel
import github.masterj3y.searchmovie.model.MovieItem
import kotlinx.coroutines.delay

@Composable
fun SearchMovieScreen(
    viewModel: SearchMovieViewModel = hiltViewModel(),
    onMovieClick: (path: String) -> Unit
) {

    val stateLifecycleAware = rememberFlowWithLifecycle(viewModel.state)
    val state by stateLifecycleAware.collectAsState(SearchMovieState.initial())

    val (movieTitle, setMovieTitle) = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(movieTitle) {
        delay(700)
        if (movieTitle.isNotBlank())
            viewModel.search(movieTitle)
    }

    Scaffold(
        topBar = {
            Surface(elevation = 4.dp) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    SearchInput(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = stringResource(R.string.search_movie_input_placeholder))
                        },
                        value = movieTitle,
                        onValueChange = setMovieTitle
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(padding)
                .fillMaxSize()
        ) {

            Crossfade(targetState = state) { state ->
                when {

                    !state.isLoading && !state.hasAnErrorOccurred && state.result.isEmpty() -> Idle()

                    state.isLoading -> Loading()

                    !state.isLoading && !state.hasAnErrorOccurred && state.result.isNotEmpty() -> {
                        Movies(
                            movies = state.result,
                            onMovieClick = { movieItem ->
                                val moviePath = movieItem.url.substringAfterLast("/")
                                onMovieClick(moviePath)
                            }
                        )
                    }

                    state.hasAnErrorOccurred -> Error()
                }
            }
        }

    }
}

@Composable
private fun Idle() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = R.drawable.ils_search), contentDescription = null)
    }
}

@Composable
private fun Movies(
    movies: Map<String, List<MovieItem>>,
    onMovieClick: (MovieItem) -> Unit
) {

    val (categoryKey, setCategoryKey) = rememberSaveable {
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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {

        tabs.forEach { tab ->
            SimpleTab(
                modifier = Modifier
                    .weight(1f / tabs.size),
                text = tab,
                isSelected = tab == selectedTab,
                unselectedColor = Color.Gray,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun MovieItem(movie: MovieItem, onClick: (MovieItem) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(movie) },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color.White.copy(alpha = .08f),
                                Color.White.copy(alpha = .02f),
                                Color.White.copy(alpha = .009f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = movie.subtitles.toString(),
                    style = MaterialTheme.typography.h5
                )
            }
            Divider(
                modifier = Modifier
                    .padding(16.dp)
                    .width(1.dp)
                    .height(32.dp)
            )
            Text(
                modifier = Modifier.padding(end = 16.dp), text = movie.title, maxLines = 2
            )
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