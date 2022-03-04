package github.masterj3y.subscene.data

import github.masterj3y.network.NetworkConstants.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface SubtitleDataSource {

    suspend fun searchMovie(title: String): Flow<String?>
}

class SubtitleDataSourceImpl
@Inject
constructor(private val httpClient: HttpClient) : SubtitleDataSource {

    override suspend fun searchMovie(title: String): Flow<String?> = flow {

        val response: String? = httpClient.post("${BASE_URL}subtitles/searchbytitle") {
            contentType(ContentType.Application.Json)
            body = SearchMovieRequestBody(query = title)
        }

        emit(response)
    }
}

@Serializable
private data class SearchMovieRequestBody(
    val query: String
)