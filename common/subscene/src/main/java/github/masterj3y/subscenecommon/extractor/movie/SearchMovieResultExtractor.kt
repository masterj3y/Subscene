package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.regexer.Regexer

class SearchMovieResultExtractor : Extractor<List<String>?> {

    override fun extract(string: String?): List<String>? {
        val regex =
            "(?<=li>\\s{1,10}<div class=\"title\">)\\s*<a href=\".*\">.*</a>\\s*</div>\\s*<div class=\"subtle count\">\\s*\\d+ subtitles?\\s*(?=</div>)".toRegex()
        return Regexer(regex).findGroups(string)
    }
}