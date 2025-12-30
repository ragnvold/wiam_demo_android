package com.nparmenov.wiam_demo

sealed interface SubmissionState {
    data object Idle: SubmissionState
    data object Loading: SubmissionState
    data class Success(val requestId: String? = null): SubmissionState
    data class Error(val message: String, val cause: Throwable? = null): SubmissionState
}