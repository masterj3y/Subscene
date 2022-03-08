package github.masterj3y.searchmovie.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import github.masterj3y.searchmovie.model.MovieItem

sealed class SearchMovieState {

    object Loading : SearchMovieState()

    data class Result(
        val movies: SnapshotStateList<MovieItem> = mutableStateListOf()
    ) : SearchMovieState()

    object Error : SearchMovieState()
}

sealed class SearchMovieEvent {

    class Search(val movieTitle: String) : SearchMovieEvent()
}

sealed class SearchMovieEffect