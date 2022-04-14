package github.masterj3y.subtitle

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.reducer.Reducer
import github.masterj3y.mvi.viewmodel.BaseViewModel
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toDownloadSubtitle
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEffect
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEvent
import github.masterj3y.subtitle.ui.download.DownloadSubtitleState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadSubtitleViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<DownloadSubtitleState, DownloadSubtitleEffect>() {

    private val reducer = DownloadReducer()

    override val state: StateFlow<DownloadSubtitleState> = reducer.state
    override val effect: Flow<DownloadSubtitleEffect> = reducer.effect

    fun initialise(subtitlePreview: SubtitlePreview) {
        reducer.sendEvent(DownloadSubtitleEvent.Initialize(subtitlePreview))
    }

    fun getDownloadPath(url: String) {
        reducer.sendEvent(DownloadSubtitleEvent.GetDownloadPath(url))
    }

    private inner class DownloadReducer :
        Reducer<DownloadSubtitleState, DownloadSubtitleEvent, DownloadSubtitleEffect>(
            DownloadSubtitleState.initial()
        ) {

        override fun reduce(currentState: DownloadSubtitleState, event: DownloadSubtitleEvent) {
            when (event) {
                is DownloadSubtitleEvent.Initialize -> initialize(event.subtitlePreview)
                is DownloadSubtitleEvent.GetDownloadPath -> getDownloadPath(event.subtitlePath)
            }
        }

        private fun initialize(subtitlePreview: SubtitlePreview) =
            setState(
                currentState.copy(
                    subtitlePreview = subtitlePreview,
                    isLoadingDownloadPath = false,
                    downloadPath = null
                )
            )

        private fun getDownloadPath(subtitlePath: String) {

            currentState
                .copy(isLoadingDownloadPath = true)
                .let(::setState)

            viewModelScope.launch {
                repository.getDownloadSubtitlePath(subtitlePath)
                    .onCompletion {
                        if (it != null) emitErrorEffect()
                    }
                    .onEach {
                        if (it == null) emitErrorEffect()
                    }
                    .catch {
                        emitErrorEffect()
                    }
                    .filterNotNull()
                    .collect {
                        currentState
                            .copy(isLoadingDownloadPath = false)
                            .let(::setState)
                        sendEffect(DownloadSubtitleEffect.PathReceived(it.toDownloadSubtitle()))
                    }
            }
        }

        private fun emitErrorEffect() = sendEffect(DownloadSubtitleEffect.Error)
    }
}