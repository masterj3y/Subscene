package github.masterj3y.subscenecommon.data

import github.masterj3y.testutils.network.engine.mockHttpClient
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SubtitleDataSourceTest {

    private val subtitleDataSource: SubtitleDataSource =
        SubtitleDataSourceImpl(mockHttpClient)

    @Test
    fun `search movie by title`() = runBlocking {
        subtitleDataSource.searchMovie("mj")
            .let {
                it shouldNotBe null
                it shouldNotBe "default"
            }
    }

    @Test
    fun `get movie details`() = runBlocking {
        subtitleDataSource.getMovieDetails("the-office-us-version-seventh-season")
            .let {
                it shouldNotBe null
                it shouldNotBe "default"
            }
    }

    @Test
    fun `get subtitle download path`() = runBlocking {
        subtitleDataSource.getSubtitleDownloadPath("subtitles/friends--fifth-season/farsi_persian/368124")
            .let {
                it shouldNotBe null
                it shouldNotBe "default"
            }
    }
}