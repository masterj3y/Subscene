package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldNotBe
import org.junit.Test

class SearchMovieResultItemExtractorTest : ResourceReader() {

    @Test
    fun testExtract() {
        val response = readApiResponse("search-movie-item-response")
        val extractor: Extractor<SearchMovieResultItem?> = SearchMovieResultItemExtractor()
        val item = extractor.extract(response)

        item shouldNotBe null
    }
}