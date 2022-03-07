package github.masterj3y.searchmovie

import github.masterj3y.subscenecommon.model.SearchMovieResultItem

sealed class SearchMovieState {

    data class Start(
        val isLoading: Boolean = false,
        val movies: List<SearchMovieResultItem> = listOf()
    ) : SearchMovieState()

    object Error : SearchMovieState()
}

sealed class SearchMovieEvent {

    class Search(val movieTitle: String) : SearchMovieEvent()
    object Reset : SearchMovieEvent()
}

sealed class SearchMovieEffect