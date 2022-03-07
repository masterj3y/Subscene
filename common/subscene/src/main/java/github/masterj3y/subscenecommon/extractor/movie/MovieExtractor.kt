package github.masterj3y.subscenecommon.extractor.movie

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem

class MovieExtractor : Extractor<List<SearchMovieResultItem>?> {

    override fun extract(string: String?): List<SearchMovieResultItem>? {

        val movieResultExtractor: Extractor<List<String>?> = SearchMovieResultExtractor()
        val movieResultItemExtractor: Extractor<SearchMovieResultItem?> =
            SearchMovieResultItemExtractor()
        return movieResultExtractor.extract(string)?.mapNotNull {
            movieResultItemExtractor.extract(it)
        }
    }
}