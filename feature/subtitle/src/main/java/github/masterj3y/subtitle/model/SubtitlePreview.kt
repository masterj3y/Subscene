package github.masterj3y.subtitle.model

import javax.annotation.concurrent.Immutable

@Immutable
data class SubtitlePreview(
    val language: String,
    val name: String,
    val url: String,
    val owner: String,
    val comment: String,
)