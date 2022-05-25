package github.masterj3y.searchmovie.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.masterj3y.resources.R
import github.masterj3y.resources.composables.SimpleTab
import github.masterj3y.searchmovie.model.MovieItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Movies(
    movies: Map<String, List<MovieItem>>,
    onMovieClick: (MovieItem) -> Unit
) {

    val (categoryKey, setCategoryKey) = remember {
        mutableStateOf(movies.keys.toList().first())
    }

    val items = remember(categoryKey) {
        mutableStateOf(movies[categoryKey])
    }

    LazyColumn {
        stickyHeader {
            Tabs(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colors.background,
                                MaterialTheme.colors.background.copy(alpha = .0f)
                            )
                        )
                    )
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                tabs = movies.keys,
                selectedTab = categoryKey,
                onClick = setCategoryKey
            )
        }
        items(items = items.value ?: listOf()) {
            MovieItem(
                movie = it,
                onClick = onMovieClick
            )
        }
    }
}

@Composable
private fun MovieItem(movie: MovieItem, onClick: (MovieItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colors.primary.copy(alpha = .1f))
            .clickable { onClick(movie) }
            .padding(16.dp)
    ) {

        Text(text = movie.title)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.movie_subtitle_count, movie.subtitles),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun Tabs(
    modifier: Modifier = Modifier,
    tabs: Set<String>,
    selectedTab: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        LazyRow {

            item {
                Spacer(modifier = Modifier.width(8.dp))
            }

            items(tabs.toList()) {
                SimpleTab(
                    text = it,
                    isSelected = it == selectedTab,
                    selectedColor = MaterialTheme.colors.background,
                    unselectedColor = MaterialTheme.colors.primary,
                    onClick = onClick
                )
            }
        }
    }
}