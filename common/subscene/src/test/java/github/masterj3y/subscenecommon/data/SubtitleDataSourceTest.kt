package github.masterj3y.subscenecommon.data

import github.masterj3y.testutils.network.engine.mockHttpClient
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SubtitleDataSourceTest {

    private val subtitleDataSource: SubtitleDataSource =
        SubtitleDataSourceImpl(mockHttpClient)

    @Test
    fun `search movie by title`() = runBlocking {
        subtitleDataSource.searchMovie("mj")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }

    @Test
    fun `get movie details`() = runBlocking {
        subtitleDataSource.getMovieDetails("some/url")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }
}