package com.nparmenov.wiam_demo

class SaveLastLoanApplicationMiddleware(
    private val loanApplicationHistory: LoanApplicationHistory
): Middleware<LoanCalculatorState, LoanAction> {

    override fun intercept(
        state: LoanCalculatorState,
        action: LoanAction,
        dispatch: (LoanAction) -> Unit
    ) {
        if (action != LoanAction.SubmissionClicked) return

        loanApplicationHistory.saveLoanApplication(
            state.loanTerms
        )
    }
}