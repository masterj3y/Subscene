package github.masterj3y.subscene.extractor.movie

import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.model.SearchMovieResultItem
import github.masterj3y.subscene.regexer.Regexer

class SearchMovieResultItemExtractor : Extractor<SearchMovieResultItem?> {

    override fun extract(string: String?): SearchMovieResultItem? {

        val itemRegex =
            "\\s*<a href=\"(.*)\">(.*)</a>\\s*</div>\\s*<div class=\"subtle count\">\\s*(\\d+) subtitles?\\s*".toRegex()
        val itemRegexer = Regexer(itemRegex)

        val groupValues = itemRegexer.extractGroupValues(string)
        val url = groupValues?.get(1)
        val title = groupValues?.get(2)
        val subtitles = groupValues?.get(3)?.toIntOrNull()

        if (url.isNullOrBlank() || title.isNullOrBlank() || subtitles == null)
            return null

        return SearchMovieResultItem(
            title = title,
            url = url,
            subtitles = subtitles
        )
    }
}