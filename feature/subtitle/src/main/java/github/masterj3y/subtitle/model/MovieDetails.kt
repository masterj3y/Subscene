package github.masterj3y.subtitle.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Immutable
data class MovieDetails(
    val poster: String,
    val title: String,
    val year: String,
    val imdb: String,
    val subtitlePreviewList: SnapshotStateList<SubtitlePreview>
)
