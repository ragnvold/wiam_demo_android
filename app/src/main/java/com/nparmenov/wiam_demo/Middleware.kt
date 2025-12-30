package com.nparmenov.wiam_demo

interface Middleware<S, A> {
    fun intercept(
        state: S,
        action: A,
        dispatch: (A) -> Unit
    )
}