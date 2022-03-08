package github.masterj3y.subscenecommon.data

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.extractor.movie.SearchMovieResultExtractor
import github.masterj3y.subscenecommon.extractor.subtitle.SubtitleItemsExtractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.subscenecommon.model.SubtitleItem
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
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?> =
        SearchMovieResultExtractor()
    private val subtitleExtractor: Extractor<List<SubtitleItem>?> =
        SubtitleItemsExtractor()
    private val subtitleRepository: SubtitleRepository = SubtitleRepositoryImpl(
        subtitleDataSource = subtitleDataSource,
        movieExtractor = movieExtractor,
        subtitleExtractor = subtitleExtractor
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
    fun testGetMovieSubtitles(): Unit = runBlocking {
        subtitleRepository.getMovieSubtitles("some/url")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }
}