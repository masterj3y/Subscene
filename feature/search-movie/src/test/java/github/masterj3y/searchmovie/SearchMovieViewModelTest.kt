package github.masterj3y.searchmovie

import github.masterj3y.searchmovie.mockdata.MockData
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchMovieViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val repository: SubtitleRepository = mock()
    private val viewModel = SearchMovieViewModel(repository, Dispatchers.Default)

    @Test
    fun `test search movie event`() = runTest {

        val mockData = MockData.mockSearchMovieResult

        whenever(repository.searchMovieByTitle("hello")).thenReturn(flowOf(mockData))

        viewModel.search("hello")

        val result = viewModel.state
            .filter {
                it.result.isNotEmpty()
            }
            .first()

        result shouldNotBe null
        result.result.size shouldBe 2
    }
}