package github.masterj3y.subtitle.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import github.masterj3y.resources.R
import github.masterj3y.subtitle.SubtitlesViewModel
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview

@Composable
fun MovieDetails(
    moviePath: String?,
    viewModel: SubtitlesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val onEvent = remember {
        { event: MovieDetailsEvent -> viewModel.onEvent(event) }
    }

    LaunchedEffect(Unit) {
        if (!moviePath.isNullOrBlank())
            onEvent(MovieDetailsEvent.Load(moviePath))
    }

    when (state) {
        is MovieDetailsState.Loading -> Loading()
        is MovieDetailsState.Result -> {
            val movieDetails = (state as? MovieDetailsState.Result)?.movieDetails
            if (movieDetails == null)
                Error()
            else
                Result(
                    movieDetails = movieDetails,
                    onSubtitlePreviewClick = {}
                )
        }
        is MovieDetailsState.Error -> Error()
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Result(movieDetails: MovieDetails, onSubtitlePreviewClick: (SubtitlePreview) -> Unit) {

    val subtitleGroups = remember(movieDetails) {
        movieDetails.subtitlePreviewList.groupBy { it.language }
    }

    val (subtitlesGroupKey, setSubtitlesGroupKey) = remember {
        mutableStateOf(subtitleGroups.keys.toList().first())
    }


    LazyColumn {

        item {
            MovieDetailsHeader(
                poster = movieDetails.poster,
                title = movieDetails.title,
                year = movieDetails.year
            )
        }

        item {
            LanguageTabs(
                languages = subtitleGroups.keys.toList(),
                selectedLanguage = subtitlesGroupKey,
                onClick = setSubtitlesGroupKey
            )
        }

        items(subtitleGroups[subtitlesGroupKey] ?: listOf()) { item ->
            SubtitlePreview(
                subtitlePreview = item,
                onClick = onSubtitlePreviewClick
            )
        }
    }
}

@Composable
private fun MovieDetailsHeader(
    poster: String,
    title: String,
    year: String
) {

    val context = LocalContext.current

    Surface(color = MaterialTheme.colors.primary) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(poster)
                        .crossfade(true)
                        .build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )

                Row {
                    Chips(text = year, color = MaterialTheme.colors.background)
                    Spacer(modifier = Modifier.width(4.dp))
                    Chips(text = "IMDB", color = Color(0XFFDBA506))
                }
            }
        }
    }
}

@Composable
fun SubtitlePreview(subtitlePreview: SubtitlePreview, onClick: (SubtitlePreview) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick(subtitlePreview) },
        color = MaterialTheme.colors.primary.copy(alpha = .1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(modifier = Modifier.padding(16.dp), text = subtitlePreview.name)
    }
}

@Composable
private fun LanguageTabs(
    languages: List<String>,
    selectedLanguage: String,
    onClick: (String) -> Unit
) {

    println("tabs composed")

    Surface(color = MaterialTheme.colors.primary) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyRow {

                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                items(languages) {
                    LanguageTab(
                        language = it,
                        isSelected = it == selectedLanguage,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageTab(language: String, isSelected: Boolean, onClick: (String) -> Unit) {

    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.primary
    )

    val borderColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background
    )

    val shape = remember {
        RoundedCornerShape(30.dp)
    }

    Surface(
        modifier = Modifier.padding(end = 4.dp),
        color = backgroundColor.value,
        shape = shape,
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier
                .background(color = backgroundColor.value)
                .border(width = 1.dp, color = borderColor.value, shape = shape)
                .clickable { onClick(language) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = contentColorFor(backgroundColor = backgroundColor.value),
            text = language
        )
    }
}

@Composable
private fun Chips(text: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp),
            text = text,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Error() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.error_text))
    }
}