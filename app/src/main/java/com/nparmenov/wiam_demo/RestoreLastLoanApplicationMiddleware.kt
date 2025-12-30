package com.nparmenov.wiam_demo

class RestoreLastLoanApplicationMiddleware(
    private val loanApplicationHistory: LoanApplicationHistory
): Middleware<LoanCalculatorState, LoanAction> {

    override fun intercept(
        state: LoanCalculatorState,
        action: LoanAction,
        dispatch: (LoanAction) -> Unit
    ) {
        if (action != LoanAction.RestoreLastLoanApplicationStarted) return

        loanApplicationHistory.getLoanApplication()?.let {
            dispatch(LoanAction.LastLoanApplicationRestored(it))
        } ?: dispatch(LoanAction.LastLoanApplicationRestored(LoanTerms()))
    }

}