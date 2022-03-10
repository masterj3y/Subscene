package github.masterj3y.subtitle.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.subtitle.SubtitlesViewModel

@Composable
fun MovieDetails(
    moviePath: String?,
    viewModel: SubtitlesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val onEvent = remember {
        { event: MovieDetailsEvent -> viewModel.onEvent(event) }
    }

    LaunchedEffect(Unit) {
        if (!moviePath.isNullOrBlank())
            onEvent(MovieDetailsEvent.Load(moviePath))
    }

    when (state) {
        is MovieDetailsState.Result -> {
            val subtitles = (state as? MovieDetailsState.Result)?.movieDetails?.subtitlePreviewList
                ?: remember { mutableStateListOf() }
            LazyColumn {
                items(subtitles) {
                    Text(text = it.toString())
                }
            }
        }
    }
}