package github.masterj3y.searchmovie.mockdata

import github.masterj3y.subscenecommon.model.SearchMovieResultItem

object MockData {

    val mockSearchMovieResult: Map<String, List<SearchMovieResultItem>>
        get() = mapOf(
            "cat1" to listOf(
                SearchMovieResultItem(
                    title = "Movie 1",
                    url = "url 1",
                    subtitles = 10
                )
            ),
            "cat2" to listOf(
                SearchMovieResultItem(
                    title = "Movie 2",
                    url = "url 2",
                    subtitles = 3
                )
            )
        )
}