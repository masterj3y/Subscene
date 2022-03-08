package github.masterj3y.subscenecommon.extractor.subtitle

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SubtitleItem
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldBe
import org.junit.Test

class SubtitleItemsExtractorTest : ResourceReader() {

    @Test
    fun extract() {
        val response = readApiResponse("subtitle-items-response")
        val extractor: Extractor<List<SubtitleItem>?> = SubtitleItemsExtractor()
        val list = extractor.extract(response)
        list?.size shouldBe 196
    }
}