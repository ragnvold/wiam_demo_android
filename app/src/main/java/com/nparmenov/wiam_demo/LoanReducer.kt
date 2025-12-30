package com.nparmenov.wiam_demo

fun loanCalculatorReduce(
    state: LoanCalculatorState,
    action: LoanAction,
    validator: LoanApplicationValidator
): LoanCalculatorState = when (action) {

    is LoanAction.LoanAmountChanged -> {
        val updatedTerms = state.loanTerms.copy(loanAmount = action.value)
        state.copy(
            loanTerms = updatedTerms,
            validation = validator.validateTerms(updatedTerms)
        )
    }

    is LoanAction.LoanTermChanged -> {
        val updatedTerms = state.loanTerms.copy(loanTerm = action.value)
        state.copy(
            loanTerms = updatedTerms,
            validation = validator.validateTerms(updatedTerms)
        )
    }

    is LoanAction.CalculationFinished -> state.copy(loanQuote = action.result)

    is LoanAction.CalculationFailed -> state.copy(loanQuote = null)

    LoanAction.SubmissionClicked -> state

    LoanAction.RestoreLastLoanApplicationStarted -> state

    is LoanAction.LastLoanApplicationRestored -> state.copy(
        loanTerms = action.loanTerms,
        validation = validator.validateTerms(action.loanTerms)
    )

    LoanAction.SubmitDismissSuccess -> state.copy(submission = SubmissionState.Idle)

    LoanAction.SubmitStarted -> state.copy(submission = SubmissionState.Loading)

    is LoanAction.SubmitSucceeded -> state.copy(submission = SubmissionState.Success(action.requestId))

    is LoanAction.SubmitFailed -> state.copy(submission = SubmissionState.Error(action.message, action.cause))
}
