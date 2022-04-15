package github.masterj3y.subtitle

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.reducer.Reducer
import github.masterj3y.mvi.viewmodel.BaseViewModel
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toMovieDetails
import github.masterj3y.subtitle.ui.details.MovieDetailsEffect
import github.masterj3y.subtitle.ui.details.MovieDetailsEvent
import github.masterj3y.subtitle.ui.details.MovieDetailsState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtitlesViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<MovieDetailsState, MovieDetailsEffect>() {

    private val reducer = MovieDetailsReducer()

    override val state: StateFlow<MovieDetailsState> = reducer.state
    override val effect: Flow<MovieDetailsEffect> = reducer.effect

    fun loadMovieDetails(moviePath: String) {
        reducer.sendEvent(MovieDetailsEvent.Load(moviePath))
    }

    fun toggleDetailsBottomSheet(subtitlePreview: SubtitlePreview?) {
        reducer.sendEvent(MovieDetailsEvent.ToggleDetailsBottomSheet(subtitlePreview))
    }

    private inner class MovieDetailsReducer :
        Reducer<MovieDetailsState, MovieDetailsEvent, MovieDetailsEffect>(
            MovieDetailsState.initial()
        ) {

        override fun reduce(currentState: MovieDetailsState, event: MovieDetailsEvent) {
            when (event) {
                is MovieDetailsEvent.Load -> loadMovieDetails(event.moviePath)
                is MovieDetailsEvent.ToggleDetailsBottomSheet -> setState(
                    currentState.copy(
                        subtitlePreviewBottomSheet = event.subtitlePreview
                    )
                )
            }
        }

        private fun loadMovieDetails(moviePath: String) {
            emitLoadingState()
            viewModelScope.launch {
                repository.getMovieDetails(moviePath)
                    .onCompletion {
                        if (it != null) emitErrorState()
                    }
                    .onEach {
                        if (it == null) emitErrorState()
                    }
                    .catch {
                        emitErrorState()
                    }
                    .filterNotNull()
                    .map {
                        it.toMovieDetails()
                    }
                    .collect {
                        emitResultState(it)
                    }
            }
        }

        private fun emitLoadingState() = setState(
            currentState.copy(
                isLoading = true,
                movieDetails = null,
                hasAnErrorOccurred = false,
                subtitlePreviewBottomSheet = null
            )
        )

        private fun emitResultState(result: MovieDetails) =
            setState(
                currentState.copy(
                    isLoading = false,
                    movieDetails = result,
                    hasAnErrorOccurred = false
                )
            )

        private fun emitErrorState() = setState(
            currentState.copy(
                isLoading = false,
                movieDetails = null,
                hasAnErrorOccurred = true
            )
        )
    }
}