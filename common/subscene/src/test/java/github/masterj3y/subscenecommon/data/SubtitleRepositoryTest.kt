package github.masterj3y.subscenecommon.data

import github.masterj3y.subscenecommon.extractor.movie.MovieDetailsExtractor
import github.masterj3y.subscenecommon.extractor.movie.SearchMovieResultExtractor
import github.masterj3y.subscenecommon.extractor.subtitle.SubtitleDownloadPathExtractor
import github.masterj3y.testutils.network.engine.mockHttpClient
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import junit.framework.TestCase
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SubtitleRepositoryTest : TestCase() {

    private val subtitleDataSource: SubtitleDataSource =
        SubtitleDataSourceImpl(mockHttpClient)

    private val subtitleRepository: SubtitleRepository = SubtitleRepositoryImpl(
        subtitleDataSource = subtitleDataSource,
        movieExtractor = SearchMovieResultExtractor(),
        subtitleExtractor = MovieDetailsExtractor(),
        subtitleDownloadPathExtractor = SubtitleDownloadPathExtractor()
    )

    @Test
    fun testSearchMovieByTitle(): Unit = runBlocking {
        subtitleRepository.searchMovieByTitle("mj")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }

    @Test
    fun testGetMovieDetails(): Unit = runBlocking {
        subtitleRepository.getMovieDetails("the-office-us-version-seventh-season")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }

    @Test
    fun testDownloadSubtitlePath(): Unit = runBlocking {
        subtitleRepository.getDownloadSubtitlePath("subtitles/friends--fifth-season/farsi_persian/368124")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
                it?.path shouldNotBe ""
            }
    }
}