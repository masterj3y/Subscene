package github.masterj3y.subtitle.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import github.masterj3y.data.model.DownloadSubtitleModel
import github.masterj3y.data.model.MovieDetailsModel
import github.masterj3y.data.model.SubtitlePreviewModel

internal fun MovieDetailsModel.toMovieDetails(): MovieDetails =
    MovieDetails(
        poster = poster,
        title = title,
        year = year,
        imdb = imdb,
        subtitlePreviewList = subtitlePreviewList.mapToSubtitlePreview()
    )

internal fun List<SubtitlePreviewModel>.mapToSubtitlePreview(): SnapshotStateList<SubtitlePreview> =
    map {
        SubtitlePreview(
            language = it.language,
            name = it.name,
            url = it.url,
            owner = it.owner,
            comment = it.comment
        )
    }.toMutableStateList()

internal fun DownloadSubtitleModel.toDownloadSubtitle(): DownloadSubtitle = DownloadSubtitle(
    path = path
)