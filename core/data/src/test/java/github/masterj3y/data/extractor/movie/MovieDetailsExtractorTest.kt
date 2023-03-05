package github.masterj3y.data.extractor.movie

import github.masterj3y.data.extractor.Extractor
import github.masterj3y.data.model.MovieDetailsModel
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldBe
import org.junit.Test

class MovieDetailsExtractorTest : ResourceReader() {

    @Test
    fun extract() {
        val response = readApiResponse("subtitle-items-response")
        val extractor: Extractor<MovieDetailsModel?> = MovieDetailsExtractor()
        val list = extractor.extract(response)
        list?.subtitlePreviewList?.size shouldBe 196
    }
}