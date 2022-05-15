package github.masterj3y.searchmovie.ui
import github.masterj3y.searchmovie.model.MovieItem
import javax.annotation.concurrent.Immutable

@Immutable
data class SearchMovieState(
    val isLoading: Boolean = false,
    val result: Map<String, List<MovieItem>>,
    val hasAnErrorOccurred: Boolean
) {

    companion object {

        fun initial() = SearchMovieState(
            isLoading = false,
            result = emptyMap(),
            hasAnErrorOccurred = false
        )
    }
}