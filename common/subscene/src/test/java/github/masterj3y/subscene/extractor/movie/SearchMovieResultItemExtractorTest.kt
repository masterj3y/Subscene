package github.masterj3y.subscene.extractor.movie

import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.model.SearchMovieResultItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldNotBe
import org.junit.Test

class SearchMovieResultItemExtractorTest : ResourceReader() {

    @Test
    fun testExtract() {
        val response = readApiResponse("search-movie-item-response.html")
        val extractor: Extractor<SearchMovieResultItem?> = SearchMovieResultItemExtractor()
        val item = extractor.extract(response)

        item shouldNotBe null
    }
}