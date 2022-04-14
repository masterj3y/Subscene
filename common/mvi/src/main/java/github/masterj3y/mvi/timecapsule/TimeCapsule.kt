package github.masterj3y.mvi.timecapsule

import github.masterj3y.mvi.base.State

interface TimeCapsule<S : State> {
    fun addState(state: S)
    fun selectState(position: Int)
    fun getStates(): List<S>
}