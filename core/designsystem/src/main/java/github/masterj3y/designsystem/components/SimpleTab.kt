package github.masterj3y.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTab(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = MaterialTheme.colors.onPrimary,
    onClick: (String) -> Unit
) {

    val color = animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor
    )

    val elevation = animateDpAsState(
        targetValue = if (isSelected) 4.dp else 0.dp
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = elevation.value
    ) {
        Text(
            modifier = Modifier
                .clickable { onClick(text) }
                .padding(horizontal = 4.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            color = color.value,
            text = text
        )
    }
}