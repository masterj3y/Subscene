package github.masterj3y.searchmovie.ui

import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.Event
import github.masterj3y.mvi.base.State
import github.masterj3y.searchmovie.model.MovieItem

data class SearchMovieState(
    val isLoading: Boolean = false,
    val result: Map<String, List<MovieItem>>,
    val hasAnErrorOccurred: Boolean
) : State {

    companion object {

        fun initial() = SearchMovieState(
            isLoading = false,
            result = emptyMap(),
            hasAnErrorOccurred = false
        )
    }
}

sealed class SearchMovieEvent : Event {

    class Search(val movieTitle: String) : SearchMovieEvent()
}

sealed class SearchMovieEffect : Effect