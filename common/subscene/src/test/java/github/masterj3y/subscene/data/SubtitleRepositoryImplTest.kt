package github.masterj3y.subscene.data

import github.masterj3y.network.NetworkModule
import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.extractor.movie.MovieExtractor
import github.masterj3y.subscene.model.SearchMovieResultItem
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import junit.framework.TestCase
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SubtitleRepositoryImplTest : TestCase() {

    private val subtitleDataSource: SubtitleDataSource =
        SubtitleDataSourceImpl(NetworkModule.ktorClient)
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?> = MovieExtractor()
    private val subtitleRepository: SubtitleRepository = SubtitleRepositoryImpl(
        subtitleDataSource = subtitleDataSource,
        movieExtractor = movieExtractor
    )

    @Test
    fun testSearchMovieByTitle(): Unit = runBlocking {
        subtitleRepository.searchMovieByTitle("hello")
            .onCompletion { cause: Throwable? ->
                cause shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }
}