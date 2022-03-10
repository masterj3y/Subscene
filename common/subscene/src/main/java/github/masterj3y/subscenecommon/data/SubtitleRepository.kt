package github.masterj3y.subscenecommon.data

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.MovieDetailsModel
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SubtitleRepository {

    fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?>

    fun getMovieDetails(movieUrl: String): Flow<MovieDetailsModel?>
}

class SubtitleRepositoryImpl(
    private val subtitleDataSource: SubtitleDataSource,
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?>,
    private val subtitleExtractor: Extractor<MovieDetailsModel?>
) : SubtitleRepository {

    override fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?> =
        subtitleDataSource.searchMovie(title).map(movieExtractor::extract)

    override fun getMovieDetails(movieUrl: String): Flow<MovieDetailsModel?> =
        subtitleDataSource.getMovieDetails(movieUrl).map(subtitleExtractor::extract)
}