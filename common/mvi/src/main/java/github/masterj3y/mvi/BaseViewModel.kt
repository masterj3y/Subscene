package github.masterj3y.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, EVENT, EFFECT>(initialState: STATE) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _effect = mutableEventFlow<Effect<EFFECT>>()
    val effect: SharedFlow<Effect<EFFECT>> = _effect.asSharedFlow()

    abstract fun onEvent(event: EVENT)

    protected fun emitState(state: STATE) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = state
        }
    }

    fun emitEffect(effect: EFFECT) {
        val newEffect = Effect(effect = effect)
        _effect.tryEmit(newEffect)
    }

    inline fun <reified T : STATE> getCurrentState(): T? = state.value as? T
}