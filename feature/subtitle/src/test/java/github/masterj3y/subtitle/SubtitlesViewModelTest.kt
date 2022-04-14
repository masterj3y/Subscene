package github.masterj3y.subtitle

import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
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

        viewModel.loadMovieDetails("fight club")

        val result = viewModel.state
            .take(2)
            .last()

        result shouldNotBe null
        result.movieDetails?.subtitlePreviewList?.size shouldBe 2
    }
}