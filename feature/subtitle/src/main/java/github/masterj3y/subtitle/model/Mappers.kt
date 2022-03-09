package github.masterj3y.subtitle.model

import github.masterj3y.subscenecommon.model.SubtitleItem

internal fun List<SubtitleItem>.mapToSubtitle(): List<Subtitle> = map {
    Subtitle(
        language = it.language,
        name = it.name,
        url = it.url,
        owner = it.owner,
        comment = it.comment
    )
}