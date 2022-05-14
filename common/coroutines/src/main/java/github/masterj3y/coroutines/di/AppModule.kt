package github.masterj3y.coroutines.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.masterj3y.coroutines.di.qualifier.ViewModelCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @ViewModelCoroutineDispatcher
    fun provideViewModelDispatcher(): CoroutineDispatcher = Dispatchers.Main
}