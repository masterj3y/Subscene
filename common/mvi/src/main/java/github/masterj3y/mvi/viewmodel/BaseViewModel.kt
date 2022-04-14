package github.masterj3y.mvi.viewmodel

import androidx.lifecycle.ViewModel
import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T : State, F : Effect> : ViewModel() {

    abstract val state: StateFlow<T>

    abstract val effect: Flow<F>
}