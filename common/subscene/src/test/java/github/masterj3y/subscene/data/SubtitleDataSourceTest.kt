package github.masterj3y.subscene.data

import github.masterj3y.network.NetworkModule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SubtitleDataSourceTest {

    private val subtitleDataSource: SubtitleDataSource =
        SubtitleDataSourceImpl(NetworkModule.ktorClient)

    @Test
    fun `search movie by title`() = runBlocking {
        subtitleDataSource.searchMovie("mj")
            .onCompletion {
                it shouldBe null
            }
            .collect {
                it shouldNotBe null
            }
    }
}