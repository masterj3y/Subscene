package github.masterj3y.subtitle.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import github.masterj3y.subtitle.model.Subtitle

sealed class SubtitlesState {

    object Loading : SubtitlesState()

    data class Result(
        val subtitles: SnapshotStateList<Subtitle> = mutableStateListOf()
    ) : SubtitlesState()

    object Error : SubtitlesState()
}

sealed class SubtitlesEvent {

    class Load(val moviePath: String) : SubtitlesEvent()
}

sealed class SubtitlesEffect