package github.masterj3y.resources.composables.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FadeAnimatedVisibility(
    visible: () -> Boolean,
    modifier: Modifier = Modifier,
    label: String = "FadeAnimatedVisibility",
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
        label = label,
        content = content
    )
}