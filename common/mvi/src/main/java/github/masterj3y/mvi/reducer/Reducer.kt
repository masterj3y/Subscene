package github.masterj3y.mvi.reducer

import github.masterj3y.mvi.BuildConfig
import github.masterj3y.mvi.base.Effect
import github.masterj3y.mvi.base.Event
import github.masterj3y.mvi.base.State
import github.masterj3y.mvi.timecapsule.TimeCapsule
import github.masterj3y.mvi.timecapsule.TimeTravelCapsule
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class Reducer<S : State, V : Event, F : Effect>(initialVal: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialVal)
    val state = _state.asStateFlow()

    val currentState: S
        get() = state.value

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }

    private val _effect = Channel<F>()
    val effect = _effect.receiveAsFlow()

    init {
        timeCapsule.addState(initialVal)
    }

    fun sendEvent(event: V) {
        reduce(_state.value, event)
    }

    fun setState(newState: S) {
        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }
    }

    fun sendEffect(effect: F) {
        _effect.trySend(effect)
    }

    abstract fun reduce(currentState: S, event: V)
}