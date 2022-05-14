package github.masterj3y.subscenecommon.data

import github.masterj3y.network.NetworkConstants.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface SubtitleDataSource {

    suspend fun searchMovie(title: String): String?

    suspend fun getMovieDetails(movieUrl: String): String?

    suspend fun getSubtitleDownloadPath(subtitlePath: String): String?
}

class SubtitleDataSourceImpl
@Inject
constructor(private val httpClient: HttpClient) : SubtitleDataSource {

    override suspend fun searchMovie(title: String): String? {

        val response: String? = httpClient.post("${BASE_URL}subtitles/searchbytitle") {
            contentType(ContentType.Application.Json)
            body = SearchMovieRequestBody(query = title)
        }

        return response
    }

    override suspend fun getMovieDetails(movieUrl: String): String? {
        return httpClient.get("${BASE_URL}subtitles/$movieUrl")
    }

    override suspend fun getSubtitleDownloadPath(subtitlePath: String): String? {
        return httpClient.get("${BASE_URL}$subtitlePath")
    }
}

@Serializable
private data class SearchMovieRequestBody(
    val query: String
)