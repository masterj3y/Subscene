package github.masterj3y.subtitle.ui.details

import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview

data class MovieDetailsState(
    val isLoading: Boolean,
    val movieDetails: MovieDetails?,
    val hasAnErrorOccurred: Boolean,
    val subtitlePreviewBottomSheet: SubtitlePreview?
) {

    companion object {

        fun initial() = MovieDetailsState(
            isLoading = false,
            movieDetails = null,
            hasAnErrorOccurred = false,
            subtitlePreviewBottomSheet = null
        )
    }
}