package com.nparmenov.wiam_demo

import java.math.BigDecimal

sealed interface LoanAction {
    data class LoanAmountChanged(val value: BigDecimal): LoanAction
    data class LoanTermChanged(val value: Int): LoanAction

    data class CalculationFinished(val result: LoanQuote): LoanAction
    data class CalculationFailed(val message: String): LoanAction

    data object SubmissionClicked: LoanAction
    data object SubmitStarted: LoanAction
    data object SubmitDismissSuccess: LoanAction

    data object RestoreLastLoanApplicationStarted: LoanAction
    data class LastLoanApplicationRestored(val loanTerms: LoanTerms): LoanAction

    data class SubmitSucceeded(val requestId: String? = null): LoanAction
    data class SubmitFailed(val message: String, val cause: Throwable? = null): LoanAction
}