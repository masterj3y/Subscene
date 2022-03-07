package github.masterj3y.searchmovie.model

import github.masterj3y.subscenecommon.model.SearchMovieResultItem

fun List<SearchMovieResultItem>.mapToMovieItem(): List<MovieItem> = map {
    MovieItem(
        title = it.title,
        url = it.url,
        subtitles = it.subtitles
    )
}