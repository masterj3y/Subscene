package github.masterj3y.subscene.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.masterj3y.subscene.data.SubtitleDataSourceImpl
import github.masterj3y.subscene.data.SubtitleRepository
import github.masterj3y.subscene.data.SubtitleRepositoryImpl
import github.masterj3y.subscene.extractor.movie.MovieExtractor
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
            movieExtractor = MovieExtractor()
        )
}