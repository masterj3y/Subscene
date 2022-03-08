package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldBe
import org.junit.Test

class MovieExtractorTest : ResourceReader() {

    @Test
    fun testExtract() {
        val response = readApiResponse("search-movie-response")
        val extractor: Extractor<List<SearchMovieResultItem>?> = MovieExtractor()
        val items = extractor.extract(response)
        items?.size shouldBe 23
    }
}