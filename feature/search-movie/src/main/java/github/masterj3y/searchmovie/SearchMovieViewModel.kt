package github.masterj3y.searchmovie

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.BaseViewModel
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
    BaseViewModel<SearchMovieState, SearchMovieEvent, SearchMovieEffect>(SearchMovieState.Result()) {

    private var lastQueriedMovieTitle: String? = null

    override fun onEvent(event: SearchMovieEvent) {
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
                    it.mapToMovieItem()
                }
                .collect {
                    emitResultState(it)
                }
        }
    }

    private fun emitLoadingState() = emitState(SearchMovieState.Loading)

    private fun emitResultState(result: List<MovieItem>) =
        emitState(SearchMovieState.Result(movies = result.toMutableStateList()))

    private fun emitErrorState() = emitState(SearchMovieState.Error)
}