package github.masterj3y.searchmovie

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.reducer.Reducer
import github.masterj3y.mvi.viewmodel.BaseViewModel
import github.masterj3y.searchmovie.model.MovieItem
import github.masterj3y.searchmovie.model.mapToMovieItem
import github.masterj3y.searchmovie.ui.SearchMovieEffect
import github.masterj3y.searchmovie.ui.SearchMovieEvent
import github.masterj3y.searchmovie.ui.SearchMovieState
import github.masterj3y.subscenecommon.data.SubtitleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<SearchMovieState, SearchMovieEffect>() {

    private var lastQueriedMovieTitle: String? = null

    private val reducer = SearchReducer()

    override val state: StateFlow<SearchMovieState> = reducer.state
    override val effect: Flow<SearchMovieEffect> = reducer.effect

    fun search(movieTitle: String) {
        reducer.sendEvent(SearchMovieEvent.Search(movieTitle))
    }

    private inner class SearchReducer :
        Reducer<SearchMovieState, SearchMovieEvent, SearchMovieEffect>(
            SearchMovieState.initial()
        ) {

        override fun reduce(currentState: SearchMovieState, event: SearchMovieEvent) {
            when (event) {
                is SearchMovieEvent.Search -> searchMovie(event.movieTitle)
            }
        }

        private fun searchMovie(movieTitle: String) {

            if (movieTitle == lastQueriedMovieTitle)
                return

            lastQueriedMovieTitle = movieTitle

            viewModelScope.launch(Dispatchers.IO) {
                ensureActive()
                emitLoadingState()
                repository.searchMovieByTitle(movieTitle)
                    .onCompletion {
                        if (it != null) emitErrorState()
                    }
                    .onEach {
                        if (it == null) emitErrorState()
                    }
                    .catch {
                        emitErrorState()
                    }
                    .filterNotNull()
                    .map {
                        it.mapValues { values ->
                            values.value.mapToMovieItem()
                        }
                    }
                    .collect {
                        emitResultState(it)
                    }
            }
        }

        private fun emitLoadingState() = setState(
            currentState.copy(
                isLoading = true,
                result = emptyMap(),
                hasAnErrorOccurred = false
            )
        )

        private fun emitResultState(result: Map<String, List<MovieItem>>?) =
            setState(
                currentState.copy(
                    isLoading = false,
                    result = result ?: emptyMap(),
                    hasAnErrorOccurred = false
                )
            )

        private fun emitErrorState() = setState(
            currentState.copy(
                isLoading = false,
                result = emptyMap(),
                hasAnErrorOccurred = true
            )
        )
    }
}