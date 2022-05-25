package github.masterj3y.subtitle.ui.details

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview

@Stable
data class MovieDetailsState(
    val isLoading: Boolean,
    val movieDetails: MovieDetails?,
    val hasAnErrorOccurred: Boolean,
) {

    val subtitlePreviewBottomSheet =
        mutableStateOf<SubtitlePreviewBottomSheet>(SubtitlePreviewBottomSheet.Hide)

    companion object {

        fun initial() = MovieDetailsState(
            isLoading = false,
            movieDetails = null,
            hasAnErrorOccurred = false
        )
    }
}

sealed class SubtitlePreviewBottomSheet {

    object Hide : SubtitlePreviewBottomSheet()
    class Show(val subtitlePreview: SubtitlePreview) : SubtitlePreviewBottomSheet()
}