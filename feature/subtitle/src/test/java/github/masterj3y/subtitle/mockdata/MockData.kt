package github.masterj3y.subtitle.mockdata

import github.masterj3y.subscenecommon.model.DownloadSubtitleModel
import github.masterj3y.subscenecommon.model.MovieDetailsModel
import github.masterj3y.subscenecommon.model.SubtitlePreviewModel
import github.masterj3y.subscenecommon.state.State

object MockData {

    val mockSubtitlesResult: State<MovieDetailsModel>
        get() = State.onSuccess(
            MovieDetailsModel(
                poster = "some link",
                title = "some title",
                year = "year",
                imdb = "imdb link",
                subtitlePreviewList = listOf(
                    SubtitlePreviewModel(
                        language = "persian",
                        name = "Fight Club",
                        url = "some url",
                        owner = "mj",
                        comment = "some comment"
                    ),
                    SubtitlePreviewModel(
                        language = "persian 2",
                        name = "Fight Club 2",
                        url = "some url 2",
                        owner = "mj 2",
                        comment = "some comment 2"
                    )
                )
            )
        )

    val mockDownloadSubtitleModel: State<DownloadSubtitleModel>
        get() = State.onSuccess(DownloadSubtitleModel(path = "some path"))
}