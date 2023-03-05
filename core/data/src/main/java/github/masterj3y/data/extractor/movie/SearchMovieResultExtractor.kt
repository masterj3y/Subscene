package github.masterj3y.data.extractor.movie

import github.masterj3y.data.extractor.Extractor
import github.masterj3y.data.model.SearchMovieResultItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class SearchMovieResultExtractor : Extractor<Map<String, List<SearchMovieResultItem>>?> {

    override fun extract(string: String?): Map<String, List<SearchMovieResultItem>>? =
        if (string != null)
            Jsoup.parseBodyFragment(string)
                .body()
                .getElementsByClass("search-result")
                .firstOrNull()
                ?.extractSearchResult()
        else null

    private fun Element.extractSearchResult(): Map<String, List<SearchMovieResultItem>> {
        val resultHash = mutableMapOf<String, List<SearchMovieResultItem>>()

        val groups = getElementsByTag("h2").map { it.text() }
        val lists = getElementsByTag("ul")

        groups.forEachIndexed { index, element ->
            val listItems = lists[index].getElementsByTag("li").mapToItemsList()
            resultHash[element] = listItems
        }

        return resultHash
    }

    private fun List<Element>.mapToItemsList(): List<SearchMovieResultItem> = map { item ->
        val link = item.select("a")
        SearchMovieResultItem(
            title = link.text(),
            url = link.attr("href"),
            subtitles = item.getElementsByClass("subtle count").text()
                .replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
        )
    }
}