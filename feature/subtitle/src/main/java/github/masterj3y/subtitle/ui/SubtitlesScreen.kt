package github.masterj3y.subtitle.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.subtitle.SubtitlesViewModel

@Composable
fun SubtitlesScreen(
    moviePath: String?,
    viewModel: SubtitlesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val onEvent = remember {
        { event: SubtitlesEvent -> viewModel.onEvent(event) }
    }

    LaunchedEffect(Unit) {
        if (!moviePath.isNullOrBlank())
            onEvent(SubtitlesEvent.Load(moviePath))
    }

    when (state) {
        is SubtitlesState.Result -> {
            val subtitles = (state as? SubtitlesState.Result)?.subtitles ?: listOf()
            LazyColumn {
                items(subtitles) {
                    Text(text = it.toString())
                }
            }
        }
    }
}