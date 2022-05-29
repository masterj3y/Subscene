package github.masterj3y.subtitle

import github.masterj3y.network.NetworkConstants
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.downloader.DownloadState
import github.masterj3y.subtitle.downloader.Downloader
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.ui.download.ProgressState
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DownloadSubtitleViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var downloader: Downloader
    private lateinit var repository: SubtitleRepository
    private lateinit var viewModel: DownloadSubtitleViewModel

    @Before
    fun setup() {
        downloader = mock()
        repository = mock()
        viewModel = DownloadSubtitleViewModel(downloader, repository, Dispatchers.Default)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test initializing event`() = runTest {

        viewModel.initialise(
            SubtitlePreview(
                language = "persian",
                name = "Fight Club",
                url = "some url",
                owner = "mj",
                comment = "some comment"
            )
        )

        val result = viewModel.state.take(2).last()

        result shouldNotBe null
        result.subtitlePreview shouldNotBe null
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test download subtitle event`() = runBlocking {

        val mockData = MockData.mockDownloadSubtitleModel

        whenever(repository.getDownloadSubtitlePath("some path")).thenReturn(flowOf(mockData))
        whenever(downloader.download(NetworkConstants.BASE_URL + mockData.data!!.path))
            .thenReturn(flowOf(DownloadState.Success))

        viewModel.downloadSubtitle("some path")

        delay(1000)

        val result = viewModel.state.take(2).last()

        with(result) {
            subtitlePreview shouldBe null
            with(downloadButtonState) {
                progressState shouldBe ProgressState.SUCCESS
                progressValue shouldBe 0f
            }
            hasAnErrorOccurred shouldBe false
        }
    }
}