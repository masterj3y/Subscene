package github.masterj3y.data.model

data class MovieDetailsModel(
    val poster: String,
    val title: String,
    val year: String,
    val imdb: String,
    val subtitlePreviewList: List<SubtitlePreviewModel>
)