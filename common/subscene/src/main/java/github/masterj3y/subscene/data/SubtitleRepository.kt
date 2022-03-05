package github.masterj3y.subscene.data

import github.masterj3y.subscene.extractor.Extractor
import github.masterj3y.subscene.model.SearchMovieResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SubtitleRepository {

    suspend fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?>
}

class SubtitleRepositoryImpl(
    private val subtitleDataSource: SubtitleDataSource,
    private val movieExtractor: Extractor<List<SearchMovieResultItem>?>
) : SubtitleRepository {

    override suspend fun searchMovieByTitle(title: String): Flow<List<SearchMovieResultItem>?> =
        subtitleDataSource.searchMovie(title).map {
            movieExtractor.extract(it)
        }
}