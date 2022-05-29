package github.masterj3y.subtitle.downloader

import kotlinx.coroutines.flow.Flow

interface Downloader {

    fun download(url: String): Flow<DownloadState>
}