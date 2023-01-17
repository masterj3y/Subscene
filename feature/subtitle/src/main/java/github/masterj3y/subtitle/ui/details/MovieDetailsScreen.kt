package github.masterj3y.subtitle.ui.details

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import github.masterj3y.extensions.compose.rememberFlowWithLifecycle
import github.masterj3y.resources.R
import github.masterj3y.resources.components.Loading
import github.masterj3y.resources.components.SimpleTab
import github.masterj3y.subtitle.SubtitlesViewModel
import github.masterj3y.subtitle.model.MovieDetails
import github.masterj3y.subtitle.model.SubtitlePreview
import github.masterj3y.subtitle.ui.download.DownloadSubtitleScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieDetails(
    moviePath: String?, viewModel: SubtitlesViewModel = hiltViewModel(), onUpClick: () -> Unit
) {

    val stateLifecycleAware = rememberFlowWithLifecycle(viewModel.state)
    val state by stateLifecycleAware.collectAsState(MovieDetailsState.initial())

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    LaunchedEffect(Unit) {
        if (!moviePath.isNullOrBlank()) viewModel.loadMovieDetails(moviePath)
    }

    LaunchedEffect(state.subtitlePreviewBottomSheet.value) {
        if (state.subtitlePreviewBottomSheet.value is SubtitlePreviewBottomSheet.Show) bottomSheetState.show()
        else bottomSheetState.hide()
    }

    // clear download subtitle screen from sheet content
    LaunchedEffect(bottomSheetState.isVisible) {
        if (!bottomSheetState.isVisible)
            viewModel.hideSubtitlePreviewBottomSheet()
    }

    BackHandler(bottomSheetState.isVisible) {
        viewModel.hideSubtitlePreviewBottomSheet()
    }

    ModalBottomSheetLayout(
        modifier = Modifier.navigationBarsPadding(),
        sheetState = bottomSheetState,
        scrimColor = Color.Black.copy(alpha = .5f),
        sheetContent = {
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 1.dp)
                    .fillMaxWidth()
            ) {
                if (state.subtitlePreviewBottomSheet.value is SubtitlePreviewBottomSheet.Show) {
                    val subtitlePreview =
                        (state.subtitlePreviewBottomSheet.value as SubtitlePreviewBottomSheet.Show).subtitlePreview
                    DownloadSubtitleScreen(subtitlePreview = subtitlePreview)
                }
            }
        },
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        Crossfade(targetState = state) { state ->
            when {
                state.isLoading -> Loading()
                !state.isLoading && !state.hasAnErrorOccurred && state.movieDetails != null -> {
                    val movieDetails = state.movieDetails
                    if (movieDetails == null) Error()
                    else Result(
                        movieDetails = movieDetails,
                        onSubtitlePreviewClick = viewModel::showSubtitlePreviewBottomSheet,
                        onUpClick = onUpClick
                    )
                }
                state.hasAnErrorOccurred -> Error()
            }
        }
    }
}

@Composable
private fun Result(
    movieDetails: MovieDetails,
    onSubtitlePreviewClick: (SubtitlePreview) -> Unit,
    onUpClick: () -> Unit
) {

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
                year = movieDetails.year,
                onUpClick = onUpClick
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
                subtitlePreview = item, onClick = onSubtitlePreviewClick
            )
        }
    }
}

@Composable
private fun MovieDetailsHeader(
    poster: String, title: String, imdb: String, year: String, onUpClick: () -> Unit
) {

    val context = LocalContext.current

    val openImdb = remember(context) {
        { imdbUrl: String ->
            Intent(
                Intent.ACTION_VIEW, Uri.parse(imdbUrl)
            ).let(context::startActivity)
        }
    }

    Surface(elevation = 4.dp) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

            IconButton(onClick = onUpClick) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(width = 90.dp, height = 150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = ImageRequest.Builder(context)
                        .placeholder(R.drawable.img_poster_placeholder)
                        .error(R.drawable.img_poster_placeholder).data(poster).crossfade(true)
                        .build(),
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
                            icon = R.drawable.ic_note,
                            text = year,
                            color = MaterialTheme.colors.background
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Chips(icon = R.drawable.ic_baseline_link_24,
                            text = "IMDB",
                            color = Color(0XFFDBA506),
                            onClick = { openImdb(imdb) })
                    }
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
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp
    ) {
        Row(modifier = Modifier
            .clickable { onClick(subtitlePreview) }
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = subtitlePreview.name)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = subtitlePreview.owner, style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(painter = painterResource(id = R.drawable.ic_download), contentDescription = null)
        }
    }
}

@Composable
private fun LanguageTabs(
    languages: List<String>, selectedLanguage: String, onClick: (String) -> Unit
) {

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
                    modifier = Modifier.padding(end = 4.dp),
                    text = it,
                    isSelected = it == selectedLanguage,
                    unselectedColor = Color.Gray,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun Chips(
    @DrawableRes icon: Int? = null, text: String, color: Color, onClick: (() -> Unit)? = null
) {

    Surface(
        color = color, shape = RoundedCornerShape(4.dp)
    ) {
        Row(modifier = Modifier
            .clickable { onClick?.invoke() }
            .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically) {

            if (icon != null) Icon(
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )

            Text(
                text = text, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun Error() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.error_text))
    }
}