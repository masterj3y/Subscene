package github.masterj3y.subtitle.downloader

import github.masterj3y.subtitle.downloader.saver.FileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class AndroidDownloader(private val cachePath: String, private val fileSaver: FileSaver) :
    Downloader {

    override fun download(url: String): Flow<DownloadState> = flow {

        var input: InputStream? = null
        var output: OutputStream? = null
        var connection: HttpURLConnection? = null

        try {

            emit(DownloadState.Progress(0))

            connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                emit(DownloadState.Failed)
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            val fileLength: Int = connection.contentLength
            val filename = connection.getFilename()
            // download the file
            input = connection.inputStream
            output = FileOutputStream(cachePath)
            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int
            while (input.read(data).also { count = it } != -1) {
                // allow canceling with back button
                total += count.toLong()
                // publishing the progress....
                if (fileLength > 0) {
                    val progress = (total * 100 / fileLength).toInt()
                    emit(DownloadState.Progress(progress))
                }
                output.write(data, 0, count)
            }

            fileSaver.save(cachePath, filename)

            emit(DownloadState.Success)

        } catch (e: Exception) {
            e.printStackTrace()
            emit(DownloadState.Failed)
        } finally {

            try {
                output?.close()
                input?.close()
            } catch (ignored: IOException) {
            }

            connection?.disconnect()
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    private fun HttpURLConnection.getFilename(): String = getHeaderField("Content-Disposition")
        ?.let { raw ->
            if (raw.contains("="))
                raw.substringAfterLast("=")
            else
                null
        } ?: (1..999999).random().toString()
}