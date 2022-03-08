package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.subscenecommon.regexer.Regexer

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