package github.masterj3y.subtitle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.coroutines.di.qualifier.ViewModelCoroutineDispatcher
import github.masterj3y.data.data.SubtitleRepository
import github.masterj3y.data.state.StateStatus
import github.masterj3y.network.NetworkConstants.BASE_URL
import github.masterj3y.subtitle.downloader.DownloadState
import github.masterj3y.subtitle.downloader.Downloader
import github.masterj3y.subtitle.model.DownloadSubtitle
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toDownloadSubtitle
import github.masterj3y.subtitle.ui.download.DownloadButtonState
import github.masterj3y.subtitle.ui.download.DownloadSubtitleState
import github.masterj3y.subtitle.ui.download.ProgressState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class DownloadSubtitleViewModel @Inject
constructor(
    private val downloader: Downloader,
    private val repository: SubtitleRepository,
    @ViewModelCoroutineDispatcher
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var downloadJob: Job? = null

    private val _state = MutableStateFlow<DownloadSubtitleState?>(DownloadSubtitleState.initial())
    val state: StateFlow<DownloadSubtitleState> = _state
        .filterNotNull()
        .stateIn(
            viewModelScope + coroutineDispatcher,
            SharingStarted.Lazily,
            DownloadSubtitleState.initial()
        )

    fun initialise(subtitlePreview: SubtitlePreview) = _state.update {
        it?.copy(subtitlePreview = subtitlePreview)
    }

    fun downloadSubtitle(url: String) {

        updateDownloadButtonState {
            progressState = ProgressState.DOWNLOADING
        }

        downloadJob?.cancel()
        downloadJob = getDownloadPathFlow(url).transform { downloadPath ->

            val downloadUrl = BASE_URL + downloadPath.path.substringAfter("/")
            emitAll(downloadFlow(downloadUrl))
        }.launchIn(viewModelScope + coroutineDispatcher)
    }

    private fun getDownloadPathFlow(url: String): Flow<DownloadSubtitle> =
        repository.getDownloadSubtitlePath(url)
            .filter {
                it.status is StateStatus.Success
            }
            .map { it.data?.toDownloadSubtitle() }
            .filterNotNull()

    private fun downloadFlow(downloadUrl: String): Flow<DownloadState> =
        downloader.download(downloadUrl).onEach(::reduceDownloadState)

    private fun reduceDownloadState(downloadState: DownloadState) {
        updateDownloadButtonState {
            when (downloadState) {
                is DownloadState.Idle -> progressState = ProgressState.IDLE
                is DownloadState.Progress -> progressValue = downloadState.progress / 100f
                is DownloadState.Success -> progressState = ProgressState.SUCCESS
                is DownloadState.Failed -> progressState = ProgressState.FAILED
            }
        }
    }

    fun resetDownloadButtonState() {
        println("button state updated")
        _state.update { it?.copy(downloadButtonState = DownloadButtonState()) }
    }

    private fun updateDownloadButtonState(block: DownloadButtonState.() -> Unit) {
        _state.update {
            it?.apply {
                downloadButtonState.apply(block)
            }
        }
    }
}