package github.masterj3y.subtitle.ui

import github.masterj3y.subtitle.model.MovieDetails

sealed class MovieDetailsState {

    object Loading : MovieDetailsState()

    data class Result(val movieDetails: MovieDetails) : MovieDetailsState()

    object Error : MovieDetailsState()
}

sealed class MovieDetailsEvent {

    class Load(val moviePath: String) : MovieDetailsEvent()
}

sealed class MovieDetailsEffect