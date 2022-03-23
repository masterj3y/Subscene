package github.masterj3y.subtitle.ui.download

import github.masterj3y.subtitle.model.DownloadSubtitle
import github.masterj3y.subtitle.model.SubtitlePreview

sealed class DownloadSubtitleState {

    data class Content(
        val subtitlePreview: SubtitlePreview? = null,
        val isLoadingDownloadPath: Boolean = false,
        val downloadPath: String? = null
    ) : DownloadSubtitleState()
}

sealed class DownloadSubtitleEvent {

    class Initialize(val subtitlePreview: SubtitlePreview) : DownloadSubtitleEvent()
    class GetDownloadPath(val subtitlePath: String) : DownloadSubtitleEvent()
}

sealed class DownloadSubtitleEffect {

    class PathReceived(val downloadSubtitle: DownloadSubtitle) : DownloadSubtitleEffect()

    object Error : DownloadSubtitleEffect()
}