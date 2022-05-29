package github.masterj3y.subtitle.ui.download

import androidx.compose.runtime.Stable
import github.masterj3y.subtitle.model.SubtitlePreview

@Stable
data class DownloadSubtitleState(
    val subtitlePreview: SubtitlePreview?,
    val downloadButtonState: DownloadButtonState,
    val hasAnErrorOccurred: Boolean,
) {

    companion object {

        fun initial() = DownloadSubtitleState(
            subtitlePreview = null,
            downloadButtonState = DownloadButtonState(),
            hasAnErrorOccurred = false
        )
    }
}