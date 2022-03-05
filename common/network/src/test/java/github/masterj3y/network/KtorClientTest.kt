package github.masterj3y.network

import github.masterj3y.testutils.network.engine.mockHttpClient
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class KtorClientTest {

    @Test
    fun `ktor client and serialization work correctly`(): Unit = runBlocking {
        val response: HttpResponse = mockHttpClient.get("https://api.ipify.org/?format=json")
        response.status shouldBe HttpStatusCode.OK
    }
}