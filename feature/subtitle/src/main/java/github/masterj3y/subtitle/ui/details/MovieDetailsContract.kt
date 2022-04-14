package github.masterj3y.subtitle.ui.details

import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.Event
import github.masterj3y.mvi.base.State
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview

data class MovieDetailsState(
    val isLoading: Boolean,
    val movieDetails: MovieDetails?,
    val hasAnErrorOccurred: Boolean,
    val subtitlePreviewBottomSheet: SubtitlePreview?
) : State {

    companion object {

        fun initial() = MovieDetailsState(
            isLoading = false,
            movieDetails = null,
            hasAnErrorOccurred = false,
            subtitlePreviewBottomSheet = null
        )
    }
}

sealed class MovieDetailsEvent : Event {

    class Load(val moviePath: String) : MovieDetailsEvent()
    class ToggleDetailsBottomSheet(val subtitlePreview: SubtitlePreview?) : MovieDetailsEvent()
}

sealed class MovieDetailsEffect : Effect