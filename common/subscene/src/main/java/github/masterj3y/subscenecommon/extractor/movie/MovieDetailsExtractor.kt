package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.MovieDetailsModel
import github.masterj3y.subscenecommon.model.SubtitlePreviewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class MovieDetailsExtractor : Extractor<MovieDetailsModel?> {

    override fun extract(string: String?): MovieDetailsModel? = if (string != null) {
        var movieDetailsModel: MovieDetailsModel? = null

        Jsoup.parseBodyFragment(string)
            .body()
            .also { element ->
                movieDetailsModel = element.extractOnlyMovieDetails()
            }
            .getElementsByTag("tbody").first()
            ?.getElementsByTag("tr")
            ?.mapNotNull { row ->
                row.extractSubtitlePreviewItems()
            }
            ?.let {
                movieDetailsModel?.copy(subtitlePreviewList = it)
            }
    } else null

    private fun Element.extractOnlyMovieDetails(): MovieDetailsModel? {
        val poster = getElementsByClass("poster").first()?.select("img")?.attr("src")
        val header = getElementsByClass("header").first()
        val (title, imdb) = header?.getElementsByTag("h2")?.let { h2 ->
            val title = h2.first()?.textNodes()?.first()?.text()
            val imdb = h2.select("a").first()?.attr("href")
            arrayOf(title, imdb)
        } ?: arrayOf("", "")

        val year = header?.getElementsByTag("li")?.first()?.textNodes()?.get(1)?.text()

        return if (poster != null && title != null && imdb != null && year != null)
            MovieDetailsModel(
                poster = poster,
                title = title.trim(),
                imdb = imdb,
                year = year,
                subtitlePreviewList = listOf()
            )
        else null
    }

    private fun Element.extractSubtitlePreviewItems(): SubtitlePreviewModel? {
        val languageAndNameData = getElementsByClass("a1").firstOrNull()
        val url = languageAndNameData?.select("a")?.attr("href")
        val (language, name) = languageAndNameData?.getElementsByTag("span")?.map { it.text() }
            ?: listOf<String?>(
                null,
                null
            )
        val owner = getElementsByClass("a5").firstOrNull()?.text()
        val comment = getElementsByClass("a6").firstOrNull()?.text()

        return if (language.isNullOrEmpty() || name.isNullOrEmpty() || url.isNullOrEmpty() || owner.isNullOrEmpty())
            null
        else
            SubtitlePreviewModel(
                language = language,
                name = name,
                url = url,
                owner = owner,
                comment = comment ?: ""
            )
    }
}