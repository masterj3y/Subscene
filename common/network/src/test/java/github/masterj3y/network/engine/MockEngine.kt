package github.masterj3y.network.engine

import io.ktor.client.engine.mock.*
import io.ktor.http.*

val mockEngine = MockEngine { request ->

    respond(
        content = """{ "ip": "127.0.0.1" }""",
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}