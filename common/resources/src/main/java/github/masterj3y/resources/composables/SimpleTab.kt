package github.masterj3y.resources.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTab(
    text: String,
    isSelected: Boolean,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = MaterialTheme.colors.background,
    onClick: (String) -> Unit
) {

    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor
    )

    val borderColor = animateColorAsState(
        targetValue = if (isSelected) unselectedColor else selectedColor
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
                .clickable { onClick(text) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = contentColorFor(backgroundColor = backgroundColor.value),
            text = text
        )
    }
}