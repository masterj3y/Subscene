package github.masterj3y.searchmovie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.coroutines.di.qualifier.ViewModelCoroutineDispatcher
import github.masterj3y.searchmovie.model.mapToMovieItem
import github.masterj3y.searchmovie.ui.SearchMovieState
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.subscenecommon.state.State
import github.masterj3y.subscenecommon.state.StateStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel
@Inject
constructor(
    private val repository: SubtitleRepository,
    @ViewModelCoroutineDispatcher
    coroutineDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchQuery = savedStateHandle.getLiveData("", "")
    val searchQuery = _searchQuery.asFlow()
        .stateIn(
            viewModelScope + coroutineDispatcher,
            SharingStarted.Lazily,
            ""
        )

    @OptIn(FlowPreview::class)
    val state: StateFlow<SearchMovieState> =
        searchQuery
            .debounce(700)
            .filter {
                it.isNotBlank()
            }
            .transform {
                emitAll(repository.searchMovieByTitle(it))
            }
            .map(::reduce)
            .stateIn(
                viewModelScope + coroutineDispatcher,
                SharingStarted.Lazily,
                SearchMovieState.initial()
            )

    fun search(movieTitle: String) {
        _searchQuery.value = movieTitle
    }

    private fun reduce(data: State<Map<String, List<SearchMovieResultItem>>?>): SearchMovieState =
        when (data.status) {
            StateStatus.Loading -> state.value.copy(isLoading = true)
            StateStatus.Success -> state.value.copy(
                isLoading = false,
                result = data.mapToMovieItems(),
                hasAnErrorOccurred = false
            )
            StateStatus.Error -> state.value.copy(
                hasAnErrorOccurred = true
            )
            StateStatus.Exception -> state.value.copy(
                hasAnErrorOccurred = true
            )
        }

    private fun State<Map<String, List<SearchMovieResultItem>>?>.mapToMovieItems() =
        data?.mapValues { it.value.mapToMovieItem() } ?: mapOf()
}