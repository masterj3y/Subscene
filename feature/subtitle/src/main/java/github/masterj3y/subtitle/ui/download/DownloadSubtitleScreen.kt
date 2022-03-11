package github.masterj3y.subtitle.ui.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.resources.R
import github.masterj3y.subtitle.DownloadSubtitleViewModel
import github.masterj3y.subtitle.model.SubtitlePreview

@Composable
internal fun DownloadSubtitleScreen(subtitlePreview: SubtitlePreview) {

    val viewModel: DownloadSubtitleViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(subtitlePreview) {
        viewModel.onEvent(DownloadSubtitleEvent.Initialize(subtitlePreview))
    }

    when (state) {
        is DownloadSubtitleState.Loading -> Loading()
        is DownloadSubtitleState.Content -> Content(
            subtitlePreview = (state as? DownloadSubtitleState.Content)?.subtitlePreview,
            onDownloadClick = {}
        )
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Content(subtitlePreview: SubtitlePreview?, onDownloadClick: (SubtitlePreview) -> Unit) {
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