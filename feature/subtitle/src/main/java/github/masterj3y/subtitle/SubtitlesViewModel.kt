package github.masterj3y.subtitle

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.BaseViewModel
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.model.Subtitle
import github.masterj3y.subtitle.model.mapToSubtitle
import github.masterj3y.subtitle.ui.SubtitlesEffect
import github.masterj3y.subtitle.ui.SubtitlesEvent
import github.masterj3y.subtitle.ui.SubtitlesState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtitlesViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<SubtitlesState, SubtitlesEvent, SubtitlesEffect>(SubtitlesState.Loading) {

    override fun onEvent(event: SubtitlesEvent) {
        when (event) {
            is SubtitlesEvent.Load -> loadSubtitles(event.moviePath)
        }
    }

    private fun loadSubtitles(moviePath: String) {
        viewModelScope.launch {
            repository.getMovieSubtitles(moviePath)
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
                    it.mapToSubtitle()
                }
                .collect {
                    emitResultState(it)
                }
        }
    }

    private fun emitLoadingState() = emitState(SubtitlesState.Loading)

    private fun emitResultState(result: List<Subtitle>) =
        emitState(SubtitlesState.Result(subtitles = result.toMutableStateList()))

    private fun emitErrorState() = emitState(SubtitlesState.Error)
}