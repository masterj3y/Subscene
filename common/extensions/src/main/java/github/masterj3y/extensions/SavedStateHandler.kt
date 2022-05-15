package github.masterj3y.extensions

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    // Synchronize the LiveData with the StateFlow
    val observer = Observer<T> { value ->
        if (stateFlow.value != value) {
            stateFlow.value = value
        }
    }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        // Stop observing the LiveData if the StateFlow completes
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        // Synchronize the StateFlow with the LiveData
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) {
                liveData.value = value
            }
        }
    }.launchIn(scope)

    return stateFlow
}