package github.masterj3y.testutils.network.engine

import io.ktor.client.engine.mock.*
import io.ktor.http.*

val mockEngine = MockEngine {
    respond(
        content = """{ "ip": "127.0.0.1" }""",
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}