package github.masterj3y.subscene.data

import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.extractor.movie.MovieExtractor
import github.masterj3y.subscene.model.SearchMovieResultItem
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
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?> = MovieExtractor()
    private val subtitleRepository: SubtitleRepository = SubtitleRepositoryImpl(
        subtitleDataSource = subtitleDataSource,
        movieExtractor = movieExtractor
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
}