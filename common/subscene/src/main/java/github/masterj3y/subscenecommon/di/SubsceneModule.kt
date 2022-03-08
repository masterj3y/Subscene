package github.masterj3y.subscenecommon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.masterj3y.subscenecommon.data.SubtitleDataSourceImpl
import github.masterj3y.subscenecommon.data.SubtitleRepository
import github.masterj3y.subscenecommon.data.SubtitleRepositoryImpl
import github.masterj3y.subscenecommon.extractor.movie.MovieExtractor
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