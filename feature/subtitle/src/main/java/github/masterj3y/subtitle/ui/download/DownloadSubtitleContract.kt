package github.masterj3y.subtitle.ui.download

import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.Event
import github.masterj3y.mvi.base.State
import github.masterj3y.subtitle.model.DownloadSubtitle
import github.masterj3y.subtitle.model.SubtitlePreview

data class DownloadSubtitleState(
    val subtitlePreview: SubtitlePreview?,
    val isLoadingDownloadPath: Boolean,
    val downloadPath: String?
) : State {

    companion object {

        fun initial() = DownloadSubtitleState(
            subtitlePreview = null,
            isLoadingDownloadPath = false,
            downloadPath = null
        )
    }
}

sealed class DownloadSubtitleEvent : Event {

    class Initialize(val subtitlePreview: SubtitlePreview) : DownloadSubtitleEvent()
    class GetDownloadPath(val subtitlePath: String) : DownloadSubtitleEvent()
}

sealed class DownloadSubtitleEffect : Effect {

    class PathReceived(val downloadSubtitle: DownloadSubtitle) : DownloadSubtitleEffect()

    object Error : DownloadSubtitleEffect()
}