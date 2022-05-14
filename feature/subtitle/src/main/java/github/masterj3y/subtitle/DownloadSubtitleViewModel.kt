package github.masterj3y.subtitle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.coroutines.di.qualifier.ViewModelCoroutineDispatcher
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subscenecommon.state.StateStatus
import github.masterj3y.subtitle.model.DownloadSubtitle
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toDownloadSubtitle
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEffect
import github.masterj3y.subtitle.ui.download.DownloadSubtitleState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class DownloadSubtitleViewModel @Inject
constructor(
    private val repository: SubtitleRepository,
    @ViewModelCoroutineDispatcher
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<DownloadSubtitleState?>(DownloadSubtitleState.initial())
    val state: StateFlow<DownloadSubtitleState> = _state
        .filterNotNull()
        .stateIn(
            viewModelScope + coroutineDispatcher,
            SharingStarted.Lazily,
            DownloadSubtitleState.initial()
        )

    private val _effect =
        MutableSharedFlow<DownloadSubtitleEffect?>(replay = 0, extraBufferCapacity = 0)
    val effect = _effect.asSharedFlow()

    fun initialise(subtitlePreview: SubtitlePreview) = _state.update {
        it?.copy(subtitlePreview = subtitlePreview)
    }

    fun getDownloadPath(url: String) {
        viewModelScope.launch(coroutineDispatcher) {

            emitLoading(true)

            getDownloadPathFlow(url)
                .collect { path ->
                    _effect.emit(DownloadSubtitleEffect.PathReceived(path))
                    emitLoading(false)
                }
        }
    }

    private fun getDownloadPathFlow(url: String): Flow<DownloadSubtitle> =
        repository.getDownloadSubtitlePath(url)
            .filter {
                it.status is StateStatus.Success
            }
            .map { it.data?.toDownloadSubtitle() }
            .filterNotNull()


    private fun emitLoading(loading: Boolean) {
        _state.update {
            _state.value?.copy(
                isLoadingDownloadPath = loading
            )
        }
    }
}