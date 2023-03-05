package github.masterj3y.data.state

sealed class StateStatus {
    object Loading : StateStatus()
    object Success : StateStatus()
    object Error : StateStatus()
    object Exception : StateStatus()
}

data class State<out T>(
    val status: StateStatus,
    val data: T? = null,
    val error: Throwable? = null
) {

    companion object {
        fun <T> onLoading(): State<T> {
            return State(status = StateStatus.Loading)
        }

        fun <T> onSuccess(data: T? = null): State<T> {
            return State(status = StateStatus.Success, data = data)
        }

        fun <T> onError(error: Throwable? = null): State<T> {
            return State(status = StateStatus.Error, error = error)
        }

        fun <T> onException(error: Throwable? = null): State<T> {
            return State(status = StateStatus.Exception, error = error)
        }

    }
}