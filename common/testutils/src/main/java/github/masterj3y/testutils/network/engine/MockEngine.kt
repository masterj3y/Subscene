package github.masterj3y.testutils.network.engine

import github.masterj3y.testutils.base.ResourceReader
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

val mockEngine = MockEngine { request ->

    val resourceReader = ResourceReader()

    when {
        request.url.fullPath.contains("subtitles/searchbytitle") -> respond(
            content = resourceReader.readApiResponse("search-movie-response"),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
        else -> respond(
            content = """ok""",
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
}

val mockHttpClient = HttpClient(mockEngine) {

    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}