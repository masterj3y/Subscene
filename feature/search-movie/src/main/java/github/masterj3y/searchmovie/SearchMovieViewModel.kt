package github.masterj3y.searchmovie

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.mvi.BaseViewModel
import github.masterj3y.subscene.data.SubtitleRepository
import github.masterj3y.subscene.model.SearchMovieResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(private val repository: SubtitleRepository) :
    BaseViewModel<SearchMovieState, SearchMovieEvent, SearchMovieEffect>(SearchMovieState.Start()) {

    override fun onEvent(event: SearchMovieEvent) {
        when (event) {
            is SearchMovieEvent.Search -> searchMovie(event.movieTitle)
            is SearchMovieEvent.Reset -> reset()
        }
    }

    private fun searchMovie(movieTitle: String) {
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
                .collect {
                    emitResultState(it)
                }
        }
    }

    private fun reset() = emitState(SearchMovieState.Start())

    private fun emitLoadingState() = getCurrentState<SearchMovieState.Start>()
        ?.copy(isLoading = true)
        ?.let(::emitState)

    private fun emitResultState(result: List<SearchMovieResultItem>) =
        getCurrentState<SearchMovieState.Start>()
            ?.copy(isLoading = false, movies = result)
            ?.let(::emitState)

    private fun emitErrorState() = emitState(SearchMovieState.Error)
}