package github.masterj3y.searchmovie.mockdata

import github.masterj3y.subscene.model.SearchMovieResultItem

object MockData {

    val mockSearchMovieResult: List<SearchMovieResultItem>
        get() = listOf(
            SearchMovieResultItem(
                title = "Movie 1",
                url = "url 1",
                subtitles = 10
            ),
            SearchMovieResultItem(
                title = "Movie 2",
                url = "url 2",
                subtitles = 3
            )
        )
}