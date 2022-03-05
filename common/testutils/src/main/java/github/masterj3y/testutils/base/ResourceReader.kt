package github.masterj3y.testutils.base

import java.io.BufferedReader

abstract class ResourceReader {

    fun readApiResponse(fileName: String): String = readResource("api-response/$fileName")

    private fun readResource(fileName: String): String {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        return inputStream.bufferedReader().use(BufferedReader::readText)
    }
}