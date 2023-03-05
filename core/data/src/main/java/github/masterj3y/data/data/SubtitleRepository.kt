package github.masterj3y.data.data

import github.masterj3y.data.extractor.Extractor
import github.masterj3y.data.model.DownloadSubtitleModel
import github.masterj3y.data.model.MovieDetailsModel
import github.masterj3y.data.model.SearchMovieResultItem
import github.masterj3y.data.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface SubtitleRepository {

    fun searchMovieByTitle(title: String): Flow<State<Map<String, List<SearchMovieResultItem>>?>>

    fun getMovieDetails(movieUrl: String): Flow<State<MovieDetailsModel?>>

    fun getDownloadSubtitlePath(subtitlePath: String): Flow<State<DownloadSubtitleModel?>>
}

class SubtitleRepositoryImpl(
    private val subtitleDataSource: SubtitleDataSource,
    private val movieExtractor: Extractor<Map<String, List<SearchMovieResultItem>>?>,
    private val subtitleExtractor: Extractor<MovieDetailsModel?>,
    private val subtitleDownloadPathExtractor: Extractor<DownloadSubtitleModel?>
) : SubtitleRepository {

    override fun searchMovieByTitle(title: String): Flow<State<Map<String, List<SearchMovieResultItem>>?>> =
        flow {
            emit(State.onLoading())
            try {
                val result = subtitleDataSource.searchMovie(title)
                emit(State.onSuccess(movieExtractor.extract(result)))
            } catch (e: Exception) {
                emit(State.onError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getMovieDetails(movieUrl: String): Flow<State<MovieDetailsModel?>> =
        flow<State<MovieDetailsModel?>> {
            emit(State.onLoading())
            try {
                val result = subtitleDataSource.getMovieDetails(movieUrl)
                emit(State.onSuccess(subtitleExtractor.extract(result)))
            } catch (e: Exception) {
                emit(State.onError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getDownloadSubtitlePath(subtitlePath: String): Flow<State<DownloadSubtitleModel?>> =
        flow<State<DownloadSubtitleModel?>> {
            emit(State.onLoading())
            try {
                val result = subtitleDataSource.getSubtitleDownloadPath(subtitlePath)
                emit(State.onSuccess(subtitleDownloadPathExtractor.extract(result)))
            } catch (e: Exception) {
                emit(State.onError(e))
                e.printStackTrace()
            }
        }.flowOn(Dispatchers.IO)
}