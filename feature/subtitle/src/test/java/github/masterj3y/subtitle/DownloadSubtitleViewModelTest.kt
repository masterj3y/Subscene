package github.masterj3y.subtitle

import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEffect
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEvent
import github.masterj3y.subtitle.ui.download.DownloadSubtitleState
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

class DownloadSubtitleViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val repository: SubtitleRepository = mock()
    private val viewModel = DownloadSubtitleViewModel(repository)

    @ExperimentalCoroutinesApi
    @Test
    fun `test initializing event`() = runTest {

        viewModel.onEvent(
            DownloadSubtitleEvent.Initialize(
                SubtitlePreview(
                    language = "persian",
                    name = "Fight Club",
                    url = "some url",
                    owner = "mj",
                    comment = "some comment"
                )
            )
        )

        val result = viewModel.state
            .filter {
                it is DownloadSubtitleState.Content
            }
            .map {
                it as DownloadSubtitleState.Content
            }
            .first()

        result shouldNotBe null
        result.subtitlePreview shouldNotBe null
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test get download path effect`() = runTest {

        val mockData = MockData.mockDownloadSubtitleModel

        whenever(repository.getDownloadSubtitlePath("some path")).thenReturn(flowOf(mockData))

        viewModel.onEvent(DownloadSubtitleEvent.GetDownloadPath("some path"))

        val result = viewModel.effect
            .map {
                it.effect
            }
            .filter {
                it is DownloadSubtitleEffect.PathReceived
            }
            .map {
                it as DownloadSubtitleEffect.PathReceived
            }
            .first()

        result shouldNotBe null
        result.downloadSubtitle.path shouldBe mockData.path
    }
}