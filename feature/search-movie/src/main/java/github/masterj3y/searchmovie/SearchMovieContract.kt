package github.masterj3y.searchmovie

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import github.masterj3y.searchmovie.model.MovieItem

sealed class SearchMovieState {

    data class Start(
        val isLoading: Boolean = false,
        val movies: SnapshotStateList<MovieItem> = mutableStateListOf()
    ) : SearchMovieState()

    object Error : SearchMovieState()
}

sealed class SearchMovieEvent {

    class Search(val movieTitle: String) : SearchMovieEvent()
    object Reset : SearchMovieEvent()
}

sealed class SearchMovieEffect