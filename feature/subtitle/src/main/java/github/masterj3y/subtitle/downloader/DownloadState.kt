package github.masterj3y.subtitle.downloader

sealed class DownloadState {

    object Idle : DownloadState()

    object Success : DownloadState()

    object Failed : DownloadState()

    data class Progress(val progress: Int) : DownloadState()
}