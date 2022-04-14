package github.masterj3y.subtitle.ui.details

import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.Event
import github.masterj3y.mvi.base.State
import github.masterj3y.subtitle.model.MovieDetails

data class MovieDetailsState(
    val isLoading: Boolean,
    val movieDetails: MovieDetails?,
    val hasAnErrorOccurred: Boolean
) : State {

    companion object {

        fun initial() = MovieDetailsState(
            isLoading = false,
            movieDetails = null,
            hasAnErrorOccurred = false
        )
    }
}

sealed class MovieDetailsEvent : Event {

    class Load(val moviePath: String) : MovieDetailsEvent()
}

sealed class MovieDetailsEffect : Effect