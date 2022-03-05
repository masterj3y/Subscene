package github.masterj3y.subscene.extractor.movie

import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.model.SearchMovieResultItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldBe
import org.junit.Test

class MovieExtractorTest : ResourceReader() {

    @Test
    fun testExtract() {
        val response = readApiResponse("search-movie-response.html")
        val extractor: Extractor<List<SearchMovieResultItem>?> = MovieExtractor()
        val items = extractor.extract(response)
        items?.size shouldBe 23
    }
}