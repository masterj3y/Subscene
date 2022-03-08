package github.masterj3y.subscenecommon.data

import github.masterj3y.subscenecommon.extractor.Extractor
import github.masterj3y.subscenecommon.model.SearchMovieResultItem
import github.masterj3y.subscenecommon.model.SubtitleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SubtitleRepository {

    fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?>

    fun getMovieSubtitles(movieUrl: String): Flow<List<SubtitleItem>?>
}

class SubtitleRepositoryImpl(
    private val subtitleDataSource: SubtitleDataSource,
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?>,
    private val subtitleExtractor: Extractor<List<SubtitleItem>?>
) : SubtitleRepository {

    override fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?> =
        subtitleDataSource.searchMovie(title).map(movieExtractor::extract)

    override fun getMovieSubtitles(movieUrl: String): Flow<List<SubtitleItem>?> =
        subtitleDataSource.getMovieSubtitles(movieUrl).map(subtitleExtractor::extract)
}