package github.masterj3y.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.masterj3y.data.data.SubtitleDataSourceImpl
import github.masterj3y.data.data.SubtitleRepository
import github.masterj3y.data.data.SubtitleRepositoryImpl
import github.masterj3y.data.extractor.movie.MovieDetailsExtractor
import github.masterj3y.data.extractor.movie.SearchMovieResultExtractor
import github.masterj3y.data.extractor.subtitle.SubtitleDownloadPathExtractor
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SubsceneModule {

    @Provides
    @Singleton
    fun provideSubtitleRepository(httpClient: HttpClient): SubtitleRepository =
        SubtitleRepositoryImpl(
            subtitleDataSource = SubtitleDataSourceImpl(httpClient),
            movieExtractor = SearchMovieResultExtractor(),
            subtitleExtractor = MovieDetailsExtractor(),
            subtitleDownloadPathExtractor = SubtitleDownloadPathExtractor()
        )
}