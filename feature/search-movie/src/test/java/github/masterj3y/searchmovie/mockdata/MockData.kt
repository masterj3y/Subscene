package github.masterj3y.searchmovie.mockdata

import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.subscenecommon.state.State

object MockData {

    val mockSearchMovieResult: State<Map<String, List<SearchMovieResultItem>>>
        get() = State.onSuccess(
            mapOf(
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
        )
}