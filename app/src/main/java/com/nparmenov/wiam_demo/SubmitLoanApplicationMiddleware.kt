package com.nparmenov.wiam_demo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.RoundingMode

class SubmitLoanApplicationMiddleware(
    private val repository: LoanRepository,
    private val validator: LoanApplicationValidator,
    private val scope: CoroutineScope,
) : Middleware<LoanCalculatorState, LoanAction> {

    override fun intercept(
        state: LoanCalculatorState,
        action: LoanAction,
        dispatch: (LoanAction) -> Unit
    ) {
        if (action != LoanAction.SubmissionClicked) return
        if (state.submission is SubmissionState.Loading) return

        when (val validation = validator.validateReadyToSubmit(state)) {
            is LoanApplicationValidation.Invalid -> {
                dispatch(
                    LoanAction.SubmitFailed(
                        message = "validation",
                        cause = ValidationThrowable(validation)
                    )
                )
                return
            }
            LoanApplicationValidation.Valid -> Unit
        }

        val quote = state.loanQuote ?: run {
            dispatch(
                LoanAction.SubmitFailed(
                    message = "validation",
                    cause = ValidationThrowable(
                        LoanApplicationValidation.Invalid(R.string.validation_wait_for_quote)
                    )
                )
            )
            return
        }

        dispatch(LoanAction.SubmitStarted)

        scope.launch {
            try {
                val response = repository.submitApplication(
                    LoanApplicationRequest(
                        amount = state.loanTerms.loanAmount.setScale(0, RoundingMode.HALF_UP).toInt(),
                        period = state.loanTerms.loanTerm,
                        totalRepayment = quote.totalRepayment.setScale(0, RoundingMode.HALF_UP).toInt()
                    )
                )
                dispatch(LoanAction.SubmitSucceeded(response.id.toString()))
            } catch (t: Throwable) {
                dispatch(LoanAction.SubmitFailed(message = t.message ?: "", cause = t))
            }
        }
    }
}

class ValidationThrowable(
    val validation: LoanApplicationValidation.Invalid
) : RuntimeException()
