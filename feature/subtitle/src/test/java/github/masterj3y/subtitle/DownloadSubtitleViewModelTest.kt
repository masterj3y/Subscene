package github.masterj3y.subtitle

import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subtitle.mockdata.MockData
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.ui.download.DownloadSubtitleEffect
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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
    private val viewModel = DownloadSubtitleViewModel(repository, Dispatchers.Default)

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
    fun `test get download path effect`() = runTest {

        val mockData = MockData.mockDownloadSubtitleModel

        whenever(repository.getDownloadSubtitlePath("some path")).thenReturn(flowOf(mockData))

        viewModel.getDownloadPath("some path")

        val result = viewModel.effect
            .filter {
                it is DownloadSubtitleEffect.PathReceived
            }
            .map {
                it as DownloadSubtitleEffect.PathReceived
            }
            .first()

        result shouldNotBe null
        result.downloadSubtitle.path shouldBe mockData.data?.path
    }
}