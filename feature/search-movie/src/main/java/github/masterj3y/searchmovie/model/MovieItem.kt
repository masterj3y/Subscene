package github.masterj3y.searchmovie.model

import androidx.compose.runtime.Immutable

@Immutable
data class MovieItem(
    val title: String, val url: String, val subtitles: Int
)