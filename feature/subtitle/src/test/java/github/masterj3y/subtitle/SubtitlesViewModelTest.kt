package github.masterj3y.subtitle

import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.subtitle.ui.SubtitlesEvent
import github.masterj3y.subtitle.ui.SubtitlesState
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
    fun `test load subtitles event`() = runTest {

        val mockData = MockData.mockSubtitlesResult

        whenever(repository.getMovieSubtitles("fight club")).thenReturn(flowOf(mockData))

        viewModel.onEvent(SubtitlesEvent.Load("fight club"))

        val result = viewModel.state
            .filter {
                it is SubtitlesState.Result
            }
            .map {
                it as SubtitlesState.Result
            }
            .first()

        result shouldNotBe null
        result.subtitles.size shouldBe 2
    }
}