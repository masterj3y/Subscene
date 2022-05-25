package github.masterj3y.subtitle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.masterj3y.coroutines.di.qualifier.ViewModelCoroutineDispatcher
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subscenecommon.model.MovieDetailsModel
import github.masterj3y.subscenecommon.state.State
import github.masterj3y.subscenecommon.state.StateStatus
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.model.toMovieDetails
import github.masterj3y.subtitle.ui.details.MovieDetailsState
import github.masterj3y.subtitle.ui.details.SubtitlePreviewBottomSheet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class SubtitlesViewModel @Inject constructor(
    private val repository: SubtitleRepository,
    @ViewModelCoroutineDispatcher
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _moviePath = MutableStateFlow("")

    private val _state = MutableStateFlow(MovieDetailsState.initial())
    val state = _state.asStateFlow()

    init {
        _moviePath
            .filter { it.isNotBlank() }
            .onEach(::fetchMovieDetails)
            .launchIn(viewModelScope + coroutineDispatcher)
    }

    fun loadMovieDetails(moviePath: String) = this._moviePath.update { moviePath }

    fun showSubtitlePreviewBottomSheet(subtitlePreview: SubtitlePreview) {
        _state.value.subtitlePreviewBottomSheet.value =
            SubtitlePreviewBottomSheet.Show(subtitlePreview)
    }

    fun hideSubtitlePreviewBottomSheet() {
        _state.value.subtitlePreviewBottomSheet.value = SubtitlePreviewBottomSheet.Hide
    }

    private fun fetchMovieDetails(path: String) {
        repository.getMovieDetails(path)
            .onEach(::reduce)
            .launchIn(viewModelScope + coroutineDispatcher)
    }

    private fun reduce(data: State<MovieDetailsModel?>) = _state.update {
        when (data.status) {
            StateStatus.Loading -> state.value.copy(isLoading = true)
            StateStatus.Success -> state.value.copy(
                isLoading = false,
                movieDetails = data.data?.toMovieDetails(),
                hasAnErrorOccurred = false
            )
            StateStatus.Error -> state.value.copy(
                hasAnErrorOccurred = true
            )
            StateStatus.Exception -> state.value.copy(
                hasAnErrorOccurred = true
            )
        }
    }

    companion object {
        private const val MOVIE_PATH = "key:movie-path"
    }
}