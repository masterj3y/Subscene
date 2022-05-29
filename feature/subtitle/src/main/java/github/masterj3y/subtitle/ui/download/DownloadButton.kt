package github.masterj3y.subtitle.ui.download

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DownloadButton(
    state: DownloadButtonState,
    idleText: String,
    downloadingText: String,
    successText: String,
    failedText: String,
    onClick: () -> Unit
) {

    val downloadProgress = animateFloatAsState(
        targetValue = state.progressValue,
        animationSpec = tween(500)
    )

    val enabled by remember {
        derivedStateOf {
            state.progressState != ProgressState.SUCCESS
        }
    }

    val color by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    )

    val elevation by animateDpAsState(
        targetValue = if (enabled) 4.dp else 0.dp
    )

    val stateEnterTransition = remember {
        scaleIn(animationSpec = tween(500))
    }

    val stateExitTransition = remember {
        scaleOut(animationSpec = tween(500))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = MaterialTheme.shapes.small,
        color = color,
        elevation = elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (enabled)
                        onClick()
                },
            contentAlignment = Alignment.Center,
        ) {
            AnimatedVisibility(
                visible = state.progressState == ProgressState.IDLE,
                enter = stateEnterTransition,
                exit = stateExitTransition
            ) {
                Text(text = idleText, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            AnimatedVisibility(
                visible = state.progressState == ProgressState.DOWNLOADING,
                enter = stateEnterTransition,
                exit = stateExitTransition
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            progress = downloadProgress.value,
                            strokeWidth = 1.dp,
                            color = Color.White
                        )
                        Text(
                            text = "${(downloadProgress.value * 100).toInt()}",
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = downloadingText)
                }
            }
            AnimatedVisibility(
                visible = state.progressState == ProgressState.SUCCESS,
                enter = stateEnterTransition,
                exit = stateExitTransition
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = successText)
                }
            }
            AnimatedVisibility(
                visible = state.progressState == ProgressState.FAILED,
                enter = stateEnterTransition,
                exit = stateExitTransition
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = failedText)
                }
            }
        }
    }
}

@Stable
class DownloadButtonState(
    progressState: ProgressState = ProgressState.IDLE,
    progressValue: Float = 0f,
) {

    var progressState by mutableStateOf(progressState)

    private var _progressValue by mutableStateOf(progressValue)
    var progressValue: Float = _progressValue
        get() = _progressValue
        set(value) {
            progressState = ProgressState.DOWNLOADING
            _progressValue = value
            field = value
        }


    companion object {

        val Saver: Saver<DownloadButtonState, *> = listSaver(
            save = {
                listOf(it.progressState.name, it.progressValue)
            },
            restore = {

                val progressState = when (it[0] as String) {
                    ProgressState.DOWNLOADING.name -> ProgressState.DOWNLOADING
                    ProgressState.SUCCESS.name -> ProgressState.SUCCESS
                    ProgressState.FAILED.name -> ProgressState.FAILED
                    else -> ProgressState.IDLE
                }

                DownloadButtonState(
                    progressState = progressState,
                    progressValue = it[1] as Float
                )
            }
        )
    }
}

enum class ProgressState {
    IDLE, DOWNLOADING, SUCCESS, FAILED
}

@Composable
fun rememberDownloadButtonState(): DownloadButtonState =
    rememberSaveable(saver = DownloadButtonState.Saver) { DownloadButtonState() }
