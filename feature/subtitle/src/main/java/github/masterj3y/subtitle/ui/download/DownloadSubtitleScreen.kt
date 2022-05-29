package github.masterj3y.subtitle.ui.download

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import github.masterj3y.extensions.compose.rememberFlowWithLifecycle
import github.masterj3y.resources.R
import github.masterj3y.subtitle.DownloadSubtitleViewModel
import github.masterj3y.subtitle.model.SubtitlePreview

@Composable
internal fun DownloadSubtitleScreen(
    subtitlePreview: SubtitlePreview,
    viewModel: DownloadSubtitleViewModel = hiltViewModel()
) {

    val stateLifecycleAware = rememberFlowWithLifecycle(viewModel.state)
    val state by stateLifecycleAware.collectAsState(DownloadSubtitleState.initial())

    LaunchedEffect(subtitlePreview) {
        viewModel.initialise(subtitlePreview)
    }

    DisposableEffect(subtitlePreview) {
        viewModel.initialise(subtitlePreview)

        onDispose(viewModel::resetDownloadButtonState)
    }

    Content(
        subtitlePreview = state.subtitlePreview,
        downloadButtonState = state.downloadButtonState,
        onDownloadClick = {
            viewModel.downloadSubtitle(it.url)
        }
    )
}

@Composable
private fun Content(
    subtitlePreview: SubtitlePreview?,
    downloadButtonState: DownloadButtonState,
    onDownloadClick: (SubtitlePreview) -> Unit
) {

    if (subtitlePreview == null) return

    Column(
        modifier = Modifier
            .background(Color.Gray.copy(alpha = .1f))
            .padding(16.dp)
    ) {

        val context = LocalContext.current

        var storagePermissionGranted by remember { mutableStateOf(isStoragePermissionGranted(context)) }

        val permissionRequestLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
                storagePermissionGranted = permissionGranted
            }

        Text(text = subtitlePreview.name)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = subtitlePreview.owner,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        if (storagePermissionGranted || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            DownloadButton(
                state = downloadButtonState,
                idleText = stringResource(id = R.string.download_subtitle_button_idle),
                downloadingText = stringResource(id = R.string.download_subtitle_button_downloading),
                successText = stringResource(id = R.string.download_subtitle_button_success),
                failedText = stringResource(id = R.string.download_subtitle_button_failed),
                onClick = { onDownloadClick(subtitlePreview) }
            )
        else
            StoragePermission(
                onRequest = {
                    permissionRequestLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            )
    }
}

@Composable
private fun StoragePermission(onRequest: () -> Unit) {

    Column {
        Text(text = stringResource(R.string.external_storage_access_permission_request_message))
        Button(modifier = Modifier.fillMaxWidth(), onClick = onRequest) {
            Text(text = stringResource(R.string.grant_external_storage_access_permission))
        }
    }
}

private fun isStoragePermissionGranted(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED