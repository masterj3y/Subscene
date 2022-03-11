package github.masterj3y.subscenecommon.extractor.subtitle

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.DownloadSubtitleModel
import github.masterj3y.testutils.base.ResourceReader
import io.kotest.matchers.shouldNotBe
import org.junit.Test

class SubtitleDownloadPathExtractorTest : ResourceReader() {

    @Test
    fun `extract subtitle download path`() {
        val response = readApiResponse("download-subtitle-response")
        val extractor: Extractor<DownloadSubtitleModel?> = SubtitleDownloadPathExtractor()
        val downloadSubtitleModel = extractor.extract(response)
        downloadSubtitleModel shouldNotBe null
        downloadSubtitleModel?.path shouldNotBe ""
    }
}