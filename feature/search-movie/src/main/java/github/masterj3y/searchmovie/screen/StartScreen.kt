package github.masterj3y.searchmovie.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.masterj3y.resources.R
import github.masterj3y.searchmovie.model.MovieItem

@Composable
internal fun StartScreen(
    isLoading: Boolean,
    movieTitle: String,
    onMovieTitleChange: (String) -> Unit,
    movies: SnapshotStateList<MovieItem>,
    onMovieClick: (MovieItem) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = movieTitle,
                onValueChange = onMovieTitleChange
            )

            ResultScreen(movies = movies, onMovieClick = onMovieClick)
        }

        if (isLoading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ResultScreen(
    movies: SnapshotStateList<MovieItem>,
    onMovieClick: (MovieItem) -> Unit
) {

    LazyColumn {
        items(movies) {
            MovieItem(movie = it, onClick = onMovieClick)
        }
    }
}

@Composable
private fun MovieItem(movie: MovieItem, onClick: (MovieItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(movie) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(text = movie.title, style = MaterialTheme.typography.h6)
        Text(text = stringResource(id = R.string.movie_subtitle_count, movie.subtitles))
    }
}