package github.masterj3y.searchmovie.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import github.masterj3y.designsystem.R

@Composable
internal fun SearchInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: (@Composable () -> Unit)? = null,
) {
    BasicTextField(
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        value = value,
        onValueChange = onValueChange,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = MaterialTheme.colors.background.copy(alpha = .3f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row {
                            AnimatedVisibility(
                                visible = value.isEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                placeholder?.invoke()
                            }
                        }
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
        }
    )
}