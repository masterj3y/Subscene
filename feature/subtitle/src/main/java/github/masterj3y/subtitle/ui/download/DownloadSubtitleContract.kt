package github.masterj3y.subtitle.ui.download

import github.masterj3y.subtitle.model.DownloadSubtitle
import github.masterj3y.subtitle.model.SubtitlePreview

data class DownloadSubtitleState(
    val subtitlePreview: SubtitlePreview?,
    val isLoadingDownloadPath: Boolean,
    val downloadPath: String?,
    val hasAnErrorOccurred: Boolean,
) {

    companion object {

        fun initial() = DownloadSubtitleState(
            subtitlePreview = null,
            isLoadingDownloadPath = false,
            downloadPath = null,
            hasAnErrorOccurred = false
        )
    }
}

sealed class DownloadSubtitleEffect {

    class PathReceived(val downloadSubtitle: DownloadSubtitle) : DownloadSubtitleEffect()
}