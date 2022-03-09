package github.masterj3y.subtitle.mockdata

import github.masterj3y.subscenecommon.model.SubtitleItem

object MockData {

    val mockSubtitlesResult: List<SubtitleItem>
        get() = listOf(
            SubtitleItem(
                language = "persian",
                name = "Fight Club",
                url = "some url",
                owner = "mj",
                comment = "some comment"
            ),
            SubtitleItem(
                language = "persian 2",
                name = "Fight Club 2",
                url = "some url 2",
                owner = "mj 2",
                comment = "some comment 2"
            )
        )
}