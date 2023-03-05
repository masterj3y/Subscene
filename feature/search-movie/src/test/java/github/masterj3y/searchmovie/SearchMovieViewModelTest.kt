package github.masterj3y.searchmovie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import github.masterj3y.data.data.SubtitleRepository
import github.masterj3y.searchmovie.mockdata.MockData
import github.masterj3y.testutils.coroutine.CoroutinesTestRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class SearchMovieViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: SubtitleRepository
    private lateinit var viewModel: SearchMovieViewModel

    @Before
    fun setup() {
        repository = mock()
        viewModel = SearchMovieViewModel(
            repository,
            Dispatchers.Default,
            SavedStateHandle()
        )
    }

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