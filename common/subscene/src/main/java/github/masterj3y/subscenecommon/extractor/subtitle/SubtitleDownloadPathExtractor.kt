package github.masterj3y.subscenecommon.extractor.subtitle

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.DownloadSubtitleModel
import org.jsoup.Jsoup

class SubtitleDownloadPathExtractor : Extractor<DownloadSubtitleModel?> {

    override fun extract(string: String?): DownloadSubtitleModel? = if (string != null)
        Jsoup.parseBodyFragment(string)
            .body()
            .getElementsByClass("download")
            .first()
            ?.select("a")
            ?.attr("href")
            ?.let {
                DownloadSubtitleModel(path = it)
            }
    else null

}