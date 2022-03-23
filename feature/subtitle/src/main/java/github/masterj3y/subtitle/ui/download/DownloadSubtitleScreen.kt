package github.masterj3y.subtitle.ui.download

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.network.NetworkConstants.BASE_URL
import github.masterj3y.resources.R
import github.masterj3y.subtitle.DownloadSubtitleViewModel
import github.masterj3y.subtitle.model.SubtitlePreview

@Composable
internal fun DownloadSubtitleScreen(subtitlePreview: SubtitlePreview) {

    val context = LocalContext.current

    val viewModel: DownloadSubtitleViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val dynEffect by viewModel.effect.collectAsState(initial = null)
    val effect = dynEffect?.effect

    val openDownloadPath = remember(context) {
        { path: String ->
            val url = BASE_URL + path.substringAfter("/")
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .let(context::startActivity)
        }
    }

    LaunchedEffect(subtitlePreview) {
        viewModel.onEvent(DownloadSubtitleEvent.Initialize(subtitlePreview))
    }

    LaunchedEffect(effect) {
        when (effect) {
            is DownloadSubtitleEffect.PathReceived -> openDownloadPath(effect.downloadSubtitle.path)
            else -> {}
        }
    }

    when (state) {
        is DownloadSubtitleState.Content -> {
            val content = state as? DownloadSubtitleState.Content ?: return
            Content(
                subtitlePreview = content.subtitlePreview,
                isDownloadingSubtitlePath = content.isLoadingDownloadPath,
                onDownloadClick = {
                    viewModel.onEvent(DownloadSubtitleEvent.GetDownloadPath(it.url))
                }
            )
        }
    }
}

@Composable
private fun Content(
    subtitlePreview: SubtitlePreview?,
    isDownloadingSubtitlePath: Boolean,
    onDownloadClick: (SubtitlePreview) -> Unit
) {

    if (subtitlePreview == null) return

    Column(
        modifier = Modifier
            .background(Color.Gray.copy(alpha = .1f))
            .padding(16.dp)
    ) {

        Text(text = subtitlePreview.name)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = subtitlePreview.owner,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onDownloadClick(subtitlePreview) }) {

            if (isDownloadingSubtitlePath)
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 1.dp,
                    color = MaterialTheme.colors.onPrimary
                )
            else
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_cloud_download_24),
                    contentDescription = null
                )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(
                    id = R.string.download_subtitle_button_text,
                    subtitlePreview.language
                )
            )
        }
    }
}