package github.masterj3y.subscenecommon.extractor.subtitle

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SubtitleItem
import org.jsoup.Jsoup

class SubtitleItemsExtractor : Extractor<List<SubtitleItem>?> {

    override fun extract(string: String?): List<SubtitleItem>? = if (string != null)
        Jsoup.parseBodyFragment(string)
            .getElementsByTag("tbody").first()
            ?.getElementsByTag("tr")?.mapNotNull { row ->
                val languageAndNameData = row.getElementsByClass("a1").firstOrNull()
                val url = languageAndNameData?.select("a")?.attr("href")
                val (language, name) = languageAndNameData?.getElementsByTag("span")
                    ?.map { it.text() } ?: listOf<String?>(
                    null,
                    null
                )
                val owner = row.getElementsByClass("a5").firstOrNull()?.text()
                val comment = row.getElementsByClass("a6").firstOrNull()?.text()

                if (language.isNullOrEmpty() || name.isNullOrEmpty() || url.isNullOrEmpty() || owner.isNullOrEmpty())
                    null
                else
                    SubtitleItem(
                        language = language,
                        name = name,
                        url = url,
                        owner = owner,
                        comment = comment ?: ""
                    )
            }
    else null
}