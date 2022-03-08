package github.masterj3y.searchmovie.screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.searchmovie.SearchMovieEvent
import github.masterj3y.searchmovie.SearchMovieState
import github.masterj3y.searchmovie.SearchMovieViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchMovieScreen(viewModel: SearchMovieViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    var movieTitle by rememberSaveable { mutableStateOf("") }

    val onEvent = remember {
        { event: SearchMovieEvent -> viewModel.onEvent(event) }
    }

    LaunchedEffect(movieTitle) {
        delay(700)
        if (movieTitle.isNotBlank())
            onEvent(SearchMovieEvent.Search(movieTitle))
    }

    BackHandler(state !is SearchMovieState.Start) {
        onEvent(SearchMovieEvent.Reset)
    }

    when (state) {
        is SearchMovieState.Start -> {
            val startState = state as? SearchMovieState.Start
            StartScreen(
                isLoading = startState?.isLoading ?: false,
                movieTitle = movieTitle,
                onMovieTitleChange = { movieTitle = it },
                movies = startState?.movies ?: remember { mutableStateListOf() },
                onMovieClick = {}
            )
        }
    }
}