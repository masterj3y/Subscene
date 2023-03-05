package github.masterj3y.data.extractor.movie

import github.masterj3y.data.extractor.Extractor
import github.masterj3y.data.model.SearchMovieResultItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldBe
import org.junit.Test

class SearchMovieResultExtractorTest : ResourceReader() {

    @Test
    fun testExtract() {
        val response = readApiResponse("search-movie-response")
        val extractor: Extractor<Map<String, List<SearchMovieResultItem>>?> =
            SearchMovieResultExtractor()
        val list = extractor.extract(response)
        println(list?.values?.flatten()?.size)
        list?.values?.flatten()?.size shouldBe 50
    }
}