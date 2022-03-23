package github.masterj3y.subtitle

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.BaseViewModel
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toDownloadSubtitle
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEffect
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEvent
import github.masterj3y.subtitle.ui.download.DownloadSubtitleState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadSubtitleViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<DownloadSubtitleState, DownloadSubtitleEvent, DownloadSubtitleEffect>(
        DownloadSubtitleState.Content()
    ) {

    override fun onEvent(event: DownloadSubtitleEvent) = when (event) {
        is DownloadSubtitleEvent.Initialize -> initialize(event.subtitlePreview)
        is DownloadSubtitleEvent.GetDownloadPath -> getDownloadPath(event.subtitlePath)
    }

    private fun initialize(subtitlePreview: SubtitlePreview) =
        emitState(DownloadSubtitleState.Content(subtitlePreview = subtitlePreview))

    private fun getDownloadPath(subtitlePath: String) {

        getCurrentState<DownloadSubtitleState.Content>()
            ?.copy(isLoadingDownloadPath = true)
            ?.let(::emitState)

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
                    getCurrentState<DownloadSubtitleState.Content>()
                        ?.copy(isLoadingDownloadPath = false)
                        ?.let(::emitState)
                    emitEffect(DownloadSubtitleEffect.PathReceived(it.toDownloadSubtitle()))
                }
        }
    }

    private fun emitErrorEffect() = emitEffect(DownloadSubtitleEffect.Error)
}