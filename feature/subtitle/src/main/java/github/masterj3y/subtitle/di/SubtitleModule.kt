package github.masterj3y.subtitle.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import github.masterj3y.subtitle.downloader.AndroidDownloader
import github.masterj3y.subtitle.downloader.Downloader
import github.masterj3y.subtitle.downloader.saver.AndroidFileSaver

@Module
@InstallIn(ViewModelComponent::class)
object SubtitleModule {

    @Provides
    @ViewModelScoped
    fun provideAndroidDownloader(@ApplicationContext context: Context): Downloader =
        AndroidDownloader(
            cachePath = context.cacheDir.path + "/sub",
            fileSaver = AndroidFileSaver(contentResolver = context.contentResolver)
        )
}