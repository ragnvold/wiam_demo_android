package com.nparmenov.wiam_demo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoanStore<S, A>(
    initialState: S,
    private val reducer: (S, A) -> S,
    private val middlewares: List<Middleware<S, A>> = emptyList()
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    fun dispatch(action: A) {
        val newState = reducer(_state.value, action)
        _state.value = newState

        middlewares.forEach { middleware ->
            middleware.intercept(
                state = newState,
                action = action,
                dispatch = ::dispatch
            )
        }
    }
}