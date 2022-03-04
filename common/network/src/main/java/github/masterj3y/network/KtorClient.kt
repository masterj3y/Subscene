package github.masterj3y.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

const val BASE_URL = "https://subscene.com/"

val ktorClient = HttpClient(Android) {

    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}