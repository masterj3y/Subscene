package github.masterj3y.subtitle

import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.subtitle.ui.details.MovieDetailsEvent
import github.masterj3y.subtitle.ui.details.MovieDetailsState
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SubtitlesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val repository: SubtitleRepository = mock()
    private val viewModel = SubtitlesViewModel(repository)

    @ExperimentalCoroutinesApi
    @Test
    fun `test load movie details event`() = runTest {

        val mockData = MockData.mockSubtitlesResult

        whenever(repository.getMovieDetails("fight club")).thenReturn(flowOf(mockData))

        viewModel.onEvent(MovieDetailsEvent.Load("fight club"))

        val result = viewModel.state
            .filter {
                it is MovieDetailsState.Result
            }
            .map {
                it as MovieDetailsState.Result
            }
            .first()

        result shouldNotBe null
        result.movieDetails.subtitlePreviewList.size shouldBe 2
    }
}