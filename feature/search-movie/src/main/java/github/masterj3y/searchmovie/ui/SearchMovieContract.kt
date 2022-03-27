package github.masterj3y.searchmovie.ui

import github.masterj3y.searchmovie.model.MovieItem

sealed class SearchMovieState {

    object Loading : SearchMovieState()

    data class Result(
        val movies: Map<String, List<MovieItem>> = mapOf()
    ) : SearchMovieState()

    object Error : SearchMovieState()
}

sealed class SearchMovieEvent {

    class Search(val movieTitle: String) : SearchMovieEvent()
}

sealed class SearchMovieEffect