package github.masterj3y.subtitle.ui.details

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import github.masterj3y.resources.R
import github.masterj3y.resources.composables.LoadingScreen
import github.masterj3y.resources.composables.SimpleTab
import github.masterj3y.subtitle.SubtitlesViewModel
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.ui.download.DownloadSubtitleScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieDetails(
    moviePath: String?,
    viewModel: SubtitlesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(Unit) {
        if (!moviePath.isNullOrBlank())
            viewModel.loadMovieDetails(moviePath)
    }

    LaunchedEffect(state.subtitlePreviewBottomSheet) {
        if (state.subtitlePreviewBottomSheet != null)
            scaffoldState.bottomSheetState.expand()
        else
            scaffoldState.bottomSheetState.collapse()
    }

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        viewModel.toggleDetailsBottomSheet(null)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 1.dp)
                    .fillMaxWidth()
            ) {
                if (state.subtitlePreviewBottomSheet != null)
                    DownloadSubtitleScreen(subtitlePreview = state.subtitlePreviewBottomSheet!!)
            }
        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {

        Crossfade(targetState = state, animationSpec = tween(700)) { newState ->
            when {
                newState.isLoading -> LoadingScreen()
                !newState.isLoading && !newState.hasAnErrorOccurred && newState.movieDetails != null -> {
                    val movieDetails = state.movieDetails
                    if (movieDetails == null)
                        Error()
                    else
                        Result(
                            movieDetails = movieDetails,
                            onSubtitlePreviewClick =
                            viewModel::toggleDetailsBottomSheet
                        )
                }
                newState.hasAnErrorOccurred -> Error()
            }
        }
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
                imdb = movieDetails.imdb,
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

        item {
            Spacer(modifier = Modifier.height(4.dp))
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
    imdb: String,
    year: String
) {

    val context = LocalContext.current

    val openImdb = remember(context) {
        { imdbUrl: String ->
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(imdbUrl)
            ).let(context::startActivity)
        }
    }

    Surface(color = MaterialTheme.colors.primary) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
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

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Chips(
                        icon = R.drawable.ic_baseline_date_range_24,
                        text = year,
                        color = MaterialTheme.colors.background
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Chips(
                        icon = R.drawable.ic_baseline_link_24,
                        text = "IMDB",
                        color = Color(0XFFDBA506),
                        onClick = { openImdb(imdb) }
                    )
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = MaterialTheme.colors.primary.copy(alpha = .1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick(subtitlePreview) }
                .padding(16.dp)
        ) {
            Text(text = subtitlePreview.name)
            Spacer(modifier = Modifier.height(8.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = subtitlePreview.owner, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
private fun LanguageTabs(
    languages: List<String>,
    selectedLanguage: String,
    onClick: (String) -> Unit
) {

    Surface(color = MaterialTheme.colors.primary) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            LazyRow {

                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                items(languages) {
                    SimpleTab(
                        text = it,
                        isSelected = it == selectedLanguage,
                        selectedColor = MaterialTheme.colors.background,
                        unselectedColor = MaterialTheme.colors.primary,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Chips(
    @DrawableRes icon: Int? = null,
    text: String,
    color: Color,
    onClick: (() -> Unit)? = null
) {

    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick?.invoke() }
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (icon != null)
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null
                )

            Text(
                text = text,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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