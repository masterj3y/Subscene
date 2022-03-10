package github.masterj3y.subtitle

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.BaseViewModel
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.toMovieDetails
import github.masterj3y.subtitle.ui.MovieDetailsEffect
import github.masterj3y.subtitle.ui.MovieDetailsEvent
import github.masterj3y.subtitle.ui.MovieDetailsState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtitlesViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<MovieDetailsState, MovieDetailsEvent, MovieDetailsEffect>(MovieDetailsState.Loading) {

    override fun onEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.Load -> loadMovieDetails(event.moviePath)
        }
    }

    private fun loadMovieDetails(moviePath: String) {
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

    private fun emitResultState(result: MovieDetails) =
        emitState(MovieDetailsState.Result(movieDetails = result))

    private fun emitErrorState() = emitState(MovieDetailsState.Error)
}