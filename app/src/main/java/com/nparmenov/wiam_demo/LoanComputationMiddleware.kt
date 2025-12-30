package com.nparmenov.wiam_demo

import java.math.BigDecimal
import java.util.Calendar

class LoanComputationMiddleware(
    private val calendar: Calendar,
    private val loanCalculator: LoanCalculator,
    private val aprPercent: BigDecimal,
    private val basePeriodDays: Int,
) : Middleware<LoanCalculatorState, LoanAction> {

    override fun intercept(
        state: LoanCalculatorState,
        action: LoanAction,
        dispatch: (LoanAction) -> Unit
    ) {
        val shouldRecalculate = when (action) {
            is LoanAction.LoanAmountChanged -> true
            is LoanAction.LoanTermChanged -> true
            is LoanAction.LastLoanApplicationRestored -> true
            else -> false
        }
        if (!shouldRecalculate) return

        try {
            val loanInterestPerPeriod = loanCalculator.calculateLoanInterestPerPeriod(
                basePercent = aprPercent,
                basePeriod = basePeriodDays,
                targetPeriod = state.loanTerms.loanTerm
            )

            val dueDate = loanCalculator.calculateLoanTerm(
                currentDate = calendar,
                periodInDays = state.loanTerms.loanTerm
            )

            val totalRepayment = loanCalculator.calculateTotalRepayment(
                amount = state.loanTerms.loanAmount,
                loanInterest = loanInterestPerPeriod
            )

            dispatch(
                LoanAction.CalculationFinished(
                    LoanQuote(
                        loanInterestPerPeriod = loanInterestPerPeriod,
                        totalRepayment = totalRepayment,
                        dueDate = dueDate
                    )
                )
            )
        } catch (t: Throwable) {
            dispatch(LoanAction.CalculationFailed(t.message ?: "Calculation error"))
        }
    }
}
